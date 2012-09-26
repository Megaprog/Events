package tomas.util.event;

public class TestEvent {

    /**
     * @param args
     */
    public static void main(String[] args) {
        //Простой тест
        EventSupport sup = new EventSupport() {
            @Override
            public String toString() {
                return "TestEventSupport";
            }
        };
        sup.addEventHandler(Event.ANY, new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                System.out.println(event);
            }
        });
        sup.addEventHandler(MyEvent.ANY, new EventHandler<MyEvent>() {
            @Override
            public void handle(MyEvent event) {
                System.err.println(event);
            }
        });
        sup.fireEvent(new MyEvent(sup));
        sup.fireEvent(new Event(sup));

        /*Output must be:
tomas.util.event.TestEvent$MyEvent[EventType [super=EventType [super=null, name=ROOT], name=MY], getSource()=TestEventSupport]
tomas.util.event.TestEvent$MyEvent[EventType [super=EventType [super=null, name=ROOT], name=MY], getSource()=TestEventSupport]
tomas.util.event.Event[EventType [super=null, name=ROOT], getSource()=TestEventSupport]
        */
    }

    @SuppressWarnings("serial")
    private static class MyEvent extends Event {
        static final EventType<MyEvent> ANY = new EventType<>(Event.ANY, "MY");

        public MyEvent(Object source) {
            super(source, ANY);
        }
    }

}
