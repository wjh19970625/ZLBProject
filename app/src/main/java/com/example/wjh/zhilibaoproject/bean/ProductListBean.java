package com.example.wjh.zhilibaoproject.bean;

public class ProductListBean {
    private int status;
    private Data[] data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Data[] getData() {
        return data;
    }

    public void setData(Data[] data) {
        this.data = data;
    }

    public class Data{
        private String id;
        //服务名称
        private String name;
        //官费
        private String officialCharge;
        //服务费
        private String serviceCharge;
        private String serviceItem;
        //服务描述
        private String description;
        //图片url
        private String picture;
        private String details;
        private long createTime;
        private long updateTime;

        public String getPicture() {
            return picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getOfficialCharge() {
            return officialCharge;
        }

        public void setOfficialCharge(String officialCharge) {
            this.officialCharge = officialCharge;
        }

        public String getServiceCharge() {
            return serviceCharge;
        }

        public void setServiceCharge(String serviceCharge) {
            this.serviceCharge = serviceCharge;
        }

        public String getServiceItem() {
            return serviceItem;
        }

        public void setServiceItem(String serviceItem) {
            this.serviceItem = serviceItem;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getDetails() {
            return details;
        }

        public void setDetails(String details) {
            this.details = details;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public long getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(long updateTime) {
            this.updateTime = updateTime;
        }
    }
}
