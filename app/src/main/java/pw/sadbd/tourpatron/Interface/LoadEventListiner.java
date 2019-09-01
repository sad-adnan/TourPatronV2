package pw.sadbd.tourpatron.Interface;

import pw.sadbd.tourpatron.PojoClass.Event;

import java.util.List;

public interface LoadEventListiner {
    void onComplete(List<Event> eventList);
    void onError(String log);
}
