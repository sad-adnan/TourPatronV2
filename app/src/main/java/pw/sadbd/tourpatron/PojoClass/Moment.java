package pw.sadbd.tourpatron.PojoClass;

import java.io.Serializable;

public class Moment implements Serializable {
    private String momentID;
    private String momentUserName;
    private String momentDate;
    private String momentImg;

    public Moment(String momentID, String momentUserName, String momentDate, String momentImg) {
        this.momentID = momentID;
        this.momentUserName = momentUserName;
        this.momentDate = momentDate;
        this.momentImg = momentImg;
    }

    public Moment(){

    }

    public String getMomentID() {
        return momentID;
    }

    public void setMomentID(String momentID) {
        this.momentID = momentID;
    }

    public String getMomentUserName() {
        return momentUserName;
    }

    public void setMomentUserName(String momentUserName) {
        this.momentUserName = momentUserName;
    }

    public String getMomentDate() {
        return momentDate;
    }

    public void setMomentDate(String momentDate) {
        this.momentDate = momentDate;
    }

    public String getMomentImg() {
        return momentImg;
    }

    public void setMomentImg(String momentImg) {
        this.momentImg = momentImg;
    }

    @Override
    public String toString() {
        return "Moment{" +
                "momentID='" + momentID + '\'' +
                ", momentUserName='" + momentUserName + '\'' +
                ", momentDate='" + momentDate + '\'' +
                ", momentImg='" + momentImg + '\'' +
                '}';
    }
}
