package cn.wzbos.rudolph.example.module_a_api.bean;

import java.io.Serializable;

/**
 * Created by wuzongbo on 2017/6/15.
 */
public class User<T> implements Serializable {
    public String id;
    public String name;
    public T body;

    public User(String id, T body) {
        this.id = id;
        this.name = name;
        this.body = body;
    }
}