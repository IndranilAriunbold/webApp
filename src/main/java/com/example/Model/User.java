package com.example.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.HashMap;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
    private Integer id;
    private String name;
    private String email;

    private HashMap allAttributes = new HashMap(18);

    public User(){}

    public User(String name, String email) {
        this.name = name;
        this.email = email;
        this.allAttributes.put("name", this.name);
        this.allAttributes.put("email", this.email);

    }

    @Override
    public String toString() {
        return String.format(
                "User[id=%s, name='%s', email='%s']",
                id, name, email);
    }

    public Integer getId() {
        return id;
    }
    /*public void setId(Integer id) {
        this.id = id;
    }*/

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

    public HashMap getAllAttributes() {
        return allAttributes;
    }


}