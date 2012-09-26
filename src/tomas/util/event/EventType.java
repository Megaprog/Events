package tomas.util.event;

import java.util.Objects;

/**
 * Тип события.
 * Каждое событие имеет некий тип.
 * Любой тип события имеет супер-тип.
 * Корневой супер-тип равен {@link EventType#ROOT}, его супер тип равен <code>null</code>.
 * Если подписаться на тип события, содержащий подтипы, то будем получать события всех подтипов.
 * @author Tomas
 *
 * @param <T> к какому классу события относится данный тип
 */
public class EventType<T extends Event> {

    /** Корневой тип события. Все другие типы являются его подтипами. */
    public static final EventType<Event> ROOT = new EventType<>(true);

    private final EventType<? super T> superType;
    private final String name;

    /**
     * Создает новый тип события, супер-тип которого равен {@link EventType#ROOT}, а имя равно <code>null</code>.
     */
    public EventType() {
        this(ROOT, null);
    }

    /**
     * Создает новый тип события с заданным типом, имя которого равно <code>null</code>.
     * @param superType супер-тип
     */
    public EventType(EventType<? super T> superType) {
        this(superType, null);
    }

    /**
     * Создает новый тип события с заданным именем, супер-тип которого равен {@link EventType#ROOT}.
     * @param name имя типа события
     */
    public EventType(String name) {
        this(ROOT, name);
    }

    /**
     * Создает новый тип события с заданным типом, и именем
     * @param superType супер-тип
     * @param name имя типа события
     */
    public EventType(EventType<? super T> superType, String name) {
        this.superType = Objects.requireNonNull(superType, "EventType must have not null SuperType");
        this.name = name;
    }

    /**
     * Специальный конструктор для создания корневого типа события
     * @param root фиктивный параметр
     */
    private EventType(boolean root) {
        this.superType = null;
        this.name = "ROOT";
    }

    /**
     * @return супер-тип данного типа события
     */
    public EventType<? super T> getSuperType() {
        return this.superType;
    }

    /**
     * @return имя данного типа события
     */
    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return "EventType [super=" + superType + ", name=" + name + "]";
    }

}
