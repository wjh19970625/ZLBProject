package com.example.wjh.zlb.bean;

public class LoginBean {
    private int status;
    private String msg;
    private Data data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data{
        private String id;
        private String nickname;
        private String name;
        private String password;
        private String phoneNumber;
        private String email;
        private String sex;
        private String birthday;
        private String address;
        private String cnId;
        private String image;
        private String createTime;
        private String upadateTime;
        private String receLoginTime;
        private int state;
        private int role;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getCnId() {
            return cnId;
        }

        public void setCnId(String cnId) {
            this.cnId = cnId;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getUpadateTime() {
            return upadateTime;
        }

        public void setUpadateTime(String upadateTime) {
            this.upadateTime = upadateTime;
        }

        public String getReceLoginTime() {
            return receLoginTime;
        }

        public void setReceLoginTime(String receLoginTime) {
            this.receLoginTime = receLoginTime;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public int getRole() {
            return role;
        }

        public void setRole(int role) {
            this.role = role;
        }
    }
}
