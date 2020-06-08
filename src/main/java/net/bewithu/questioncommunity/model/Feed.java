package net.bewithu.questioncommunity.model;

import com.alibaba.fastjson.JSONObject;

import java.util.Date;

public class Feed {
    private int id;
    private int userId;
    private int type;
    private Date createdDate;
    // 存储序列化数据
    private String data;
    // 将序列化对象转换为json对象方便获取数据
    private JSONObject jsonObject;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
        this.jsonObject = JSONObject.parseObject(data);

    }

    public String get(String key) {
        return jsonObject == null ? null : String.valueOf(jsonObject.get(key));
    }
}
