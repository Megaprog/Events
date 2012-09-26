package tomas.util.event;

import java.util.EventListener;

/**
 * Основной слушатель событий.
 * Содержит единый для всех обработчик событий {@link EventHandler#handle(Event)}
 * @author Tomas
 *
 * @param <T> тип обрабатываемого события
 */
public interface EventHandler<T extends Event> extends EventListener {

    /**
     * Обрабатывает заданное событие
     * @param event событие
     */
    void handle(T event);
}
