package tomas.util.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Класс поддержки слушателей и рассылки событий.
 * Рассчитан на работу как в одном потоке, так и в многопоточной среде.
 * Для работы в одном потоке предназначены методы базового класса:
 * {@link EventSupport#addEventHandler(EventType, EventHandler)},
 * {@link EventSupport#removeEventHandler(EventType, EventHandler)},
 * {@link EventSupport#fireEvent(Event)}.
 * В многопоточной среде следут использовать методы:
 * {@link EventSupportSync#syncAddEventHandler(EventType, EventHandler)},
 * {@link EventSupportSync#syncRemoveEventHandler(EventType, EventHandler)},
 * {@link EventSupportSync#syncFireEvent(Event)}.
 * В качестве слушателей должен использовать {@link EventHandler} и его потомки.
 * Если предполагается использовать других слушателей,
 * нужно переопределить {@link EventSupport#proccessListener(EventHandler, Event)}.
 * Для хранения данных используются {@link HashMap} и {@link ArrayList}.
 * Чтобы использовать другие структуры данных, нужно переопределить
 * {@link EventSupport#getMap()} и (или) {@link EventSupport#getCollection()}
 *
 * @author Tomas
  */
public class EventSupportSync extends EventSupport {
    protected final ReadWriteLock rwl = new ReentrantReadWriteLock();

    /**
     * Подписывает слушателя на событий заданного типа и подтипов.
     * Во время выполения накладывается блокировка на запись.
     * @param eventType тип слушаемых событий (слушаются также события поддтипов заданного)
     * @param eventHandler слушатель
     */
    public <E extends Event> void syncAddEventHandler(EventType<E> eventType, EventHandler<? super E> eventHandler) {
        rwl.writeLock().lock();
        try {
            addEventHandler(eventType, eventHandler);
        }
        finally {
            rwl.writeLock().unlock();
        }
    }

    /**
     * Отписывает слушателя на событий заданного типа и подтипов
     * Во время выполения накладывается блокировка на запись.
     * @param eventType тип слушаемых событий
     * @param eventHandler слушатель
     */
    public <E extends Event> void syncRemoveEventHandler(EventType<E> eventType, EventHandler<? super E> eventHandler)  {
        rwl.writeLock().lock();
        try {
            removeEventHandler(eventType, eventHandler);
        }
        finally {
            rwl.writeLock().unlock();
        }
    }

    /**
     * Рассылает заданное событие всем слушателям подписанным на тип этого события либо родительский тип.
     * Во время выполения накладывается блокировка на чтение.
     * @param event событие
     */
    public void syncFireEvent(Event event) {
        rwl.readLock().lock();
        try {
            fireEvent(event);
        }
        finally {
            rwl.readLock().unlock();
        }
    }

}
