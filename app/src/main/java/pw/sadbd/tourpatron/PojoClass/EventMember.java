package pw.sadbd.tourpatron.PojoClass;

public class EventMember {
    private String UserID;
    private String memberid;

    public EventMember(String userID, String memberid) {
        UserID = userID;
        this.memberid = memberid;
    }

    public EventMember() {
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getMemberid() {
        return memberid;
    }

    public void setMemberid(String memberid) {
        this.memberid = memberid;
    }

    @Override
    public String toString() {
        return "EventMember{" +
                "UserID='" + UserID + '\'' +
                ", memberid='" + memberid + '\'' +
                '}';
    }
}
