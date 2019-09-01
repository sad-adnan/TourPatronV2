package pw.sadbd.tourpatron.PojoClass;

import java.io.Serializable;

public class Event implements Serializable {
    private EventDetails Details;


    public Event(EventDetails details) {
        Details = details;

    }

    public Event() {

    }

    public EventDetails getDetails() {
        return Details;
    }

    public void setDetails(EventDetails details) {
        Details = details;
    }


    @Override
    public String toString() {
        return "Event{" +
                "Details=" + Details +
                '}';
    }
}
