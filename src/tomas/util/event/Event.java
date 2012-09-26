package tomas.util.event;

import java.util.EventObject;

/**
 * Базовое событие.
 * Содержит источник события и тип события.
 * Если подписаться на тип события, содержащий подтипы, то будем получать события всех подтипов.
 * @author Tomas
 */
public class Event extends EventObject {
    private static final long serialVersionUID = 5581666996958698141L;

    /** Тип базового события. {@link Event#ANY} равен {@link EventType#ROOT} */
    public static final EventType<Event> ANY = EventType.ROOT;

    /** Тип данного события */
    protected EventType<? extends Event> eventType;

    /**
     * Создает событие с заданным источником и типом равным {@link Event#ANY}
     * @param source источник события
     */
    public Event(Object source) {
        this(source, ANY);
    }

    /**
     * Создает событие с заданным источником и типом
     * @param source источник события
     * @param eventType тип события
     */
    public Event(Object source, EventType<? extends Event> eventType) {
        super(source);
        this.eventType = eventType;
    }

    /**
     * @return Тип события
     */
    public EventType<? extends Event> getEventType() {
        return this.eventType;
    }

    @Override
    public String toString() {
        return getClass().getName() + "[" + eventType + ", getSource()=" + getSource() + "]";
    }

}
