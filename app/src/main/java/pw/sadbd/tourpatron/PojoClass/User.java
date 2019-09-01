package pw.sadbd.tourpatron.PojoClass;

import java.io.Serializable;

public class User implements Serializable {
    private String uid;
    private String name;
    private String email;
    private String phone;
    private String imgURL;

    public User(String uid, String name, String email, String phone, String imgURL) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.imgURL = imgURL;
    }

    public User(String name, String email, String phone, String imgURL) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.imgURL = imgURL;
    }

    public User() {

    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    @Override
    public String toString() {
        return "User{" +
                "uid='" + uid + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", imgURL='" + imgURL + '\'' +
                '}';
    }
}
