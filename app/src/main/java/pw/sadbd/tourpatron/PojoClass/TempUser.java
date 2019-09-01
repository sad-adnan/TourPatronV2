package pw.sadbd.tourpatron.PojoClass;

class TempUser_ {
    private String uid;
   private String username;
   private String phone;
   private String profileImg;

    public TempUser_(String uid, String username, String phone, String profileImg) {
        this.uid = uid;
        this.username = username;
        this.phone = phone;
        this.profileImg = profileImg;
    }

    public TempUser_() {

    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    @Override
    public String toString() {
        return "TempUser{" +
                "uid='" + uid + '\'' +
                ", username='" + username + '\'' +
                ", phone='" + phone + '\'' +
                ", profileImg='" + profileImg + '\'' +
                '}';
    }
}
