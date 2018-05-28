package com.blueocean.azbrain.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.io.IOException;

/**
 * C端token存储对象
 */
@Data
public class SessionObject {
    private Integer userId;
    private String wxid;
    private Long timestamp;

    public SessionObject(){
        this.timestamp = System.currentTimeMillis();
    }

    public SessionObject(int userId, String wxid){
        this.userId = userId;
        this.wxid = wxid;
        this.timestamp = System.currentTimeMillis();
    }

    public static SessionObject asSessionObject(String json){
        assert json != null;

        try {
            return (new ObjectMapper()).readValue(json, SessionObject.class);
        } catch (IOException e) {
            return null;
        }
    }

    public String toJson(){
        try {
            return (new ObjectMapper()).writeValueAsString(this);
        } catch (IOException e) {
            return null;
        }
    }
}
