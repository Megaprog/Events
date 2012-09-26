package tomas.util.event;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Класс поддержки слушателей и рассылки событий.
 * В качестве слушателей должен использовать {@link EventHandler} и его потомки.
 * Если предполагается использовать других слушателей,
 * нужно переопределить {@link EventSupport#proccessListener(EventHandler, Event)}.
 * Для хранения данных используются {@link HashMap} и {@link ArrayList}.
 * Чтобы использовать другие структуры данных, нужно переопределить
 * {@link EventSupport#getMap()} и (или) {@link EventSupport#getCollection()}
 * Рассчитан на работу только в одном потоке.
 * Для многопоточной обработки предназначен дочерний класс {@link EventSupportSync}.
 *
 * @author Tomas
 */
public class EventSupport {
    protected Map<EventType<? extends Event>, Collection<EventHandler<? extends Event>>> handlers;

    /**
     * Подписывает слушателя на событий заданного типа и подтипов
     * @param eventType тип слушаемых событий (слушаются также события поддтипов заданного)
     * @param eventHandler слушатель
     */
    public <E extends Event> void addEventHandler(EventType<E> eventType, EventHandler<? super E> eventHandler) {
        if (this.handlers == null) {
            handlers = this.getMap();
        }
        Collection<EventHandler<? extends Event>> col = handlers.get(eventType);
        //Если нет коллекции слушателей, добавляем
        if (col == null) {
            col = this.getCollection();
            handlers.put(eventType, col);
        }
        col.add((EventHandler) eventHandler);
    }

    /**
     * Отписывает слушателя на событий заданного типа и подтипов
     * @param eventType тип слушаемых событий
     * @param eventHandler слушатель
     */
    public <E extends Event> void removeEventHandler(EventType<E> eventType, EventHandler<? super E> eventHandler)  {
        if (this.handlers != null) {
            Collection<EventHandler<? extends Event>> col = handlers.get(eventType);
            if (col != null) {
                col.remove(eventHandler);
                //Если в коллекции не осталось слушателей, удаляем ее
                if (col.isEmpty()) {
                    handlers.remove(eventType);
                    //Если вообще не осталось обработчиков, удаляем всю карту
                    if (handlers.isEmpty()) {
                        handlers = null;
                    }
                }
            }
        }
    }

    /**
     * Рассылает заданное событие всем слушателям подписанным на тип этого события либо родительский тип
     * @param event событие
     */
    public void fireEvent(Event event) {
        Map<EventType<? extends Event>, Collection<EventHandler<? extends Event>>> h = this.handlers;
        if (h != null) {
            EventType<? extends Event> eventType = event.getEventType();
            do {
                Collection<EventHandler<? extends Event>> col = h.get(eventType);
                if (col != null) {
                    for (EventHandler<? extends Event> listener : col) {
                        this.proccessListener(listener, event);
                    }
                }
                eventType = eventType.getSuperType();
            } while (eventType != null);
        }
    }

    /**
     * @return карта для хранения данных. Нужно переопределить для использования другой карты
     */
    protected Map<EventType<? extends Event>, Collection<EventHandler<? extends Event>>> getMap() {
        return new HashMap<>();
    }

    /**
     * @return коллекция для хранения данных. Нужно переопределить для использования другой коллекции
     */
    protected Collection<EventHandler<? extends Event>> getCollection() {
        return new ArrayList<>();
    }

    /**
     * Вызывает соответствующий метод слушателя для отправки ему события.
     * По умолчанию вызывается {@link EventHandler#handle(Event)}.
     * Для работы с другими слушателями данный метод нужно переопределить.
     * @param listener слушатель получающий событие
     * @param event событие
     */
    @SuppressWarnings("unchecked")
    protected void proccessListener(EventHandler<? extends Event> listener, Event event) {
        ((EventHandler<Event>)listener).handle(event);
    }

}
