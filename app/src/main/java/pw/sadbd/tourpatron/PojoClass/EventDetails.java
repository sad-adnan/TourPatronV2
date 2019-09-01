package pw.sadbd.tourpatron.PojoClass;

import java.io.Serializable;
import java.util.Date;

public class EventDetails implements Serializable {

        private String eventId;
        private String eventName;
        private String eventAddress;
        private String eventBudet;
        private Date eventStartDate;
        private Date eventEndDate;

        public EventDetails(String eventId, String eventName, String eventAddress, String eventBudet, Date eventStartDate, Date eventEndDate) {
            this.eventId = eventId;
            this.eventName = eventName;
            this.eventAddress = eventAddress;
            this.eventBudet = eventBudet;
            this.eventStartDate = eventStartDate;
            this.eventEndDate = eventEndDate;
        }

        public EventDetails() {

        }

        public String getEventId() {
            return eventId;
        }

        public void setEventId(String eventId) {
            this.eventId = eventId;
        }

        public String getEventName() {
            return eventName;
        }

        public void setEventName(String eventName) {
            this.eventName = eventName;
        }

        public String getEventAddress() {
            return eventAddress;
        }

        public void setEventAddress(String eventAddress) {
            this.eventAddress = eventAddress;
        }

        public String getEventBudet() {
            return eventBudet;
        }

        public void setEventBudet(String eventBudet) {
            this.eventBudet = eventBudet;
        }

        public Date getEventStartDate() {
            return eventStartDate;
        }

        public void setEventStartDate(Date eventStartDate) {
            this.eventStartDate = eventStartDate;
        }

        public Date getEventEndDate() {
            return eventEndDate;
        }

        public void setEventEndDate(Date eventEndDate) {
            this.eventEndDate = eventEndDate;
        }

        @Override
        public String toString() {
            return "EventObj{" +
                    "eventId='" + eventId + '\'' +
                    ", eventName='" + eventName + '\'' +
                    ", eventAddress='" + eventAddress + '\'' +
                    ", eventBudet='" + eventBudet + '\'' +
                    ", eventStartDate=" + eventStartDate +
                    ", eventEndDate=" + eventEndDate +
                    '}';
        }


}
