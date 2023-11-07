package com.wohl.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wohl.entity.std.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class User extends Entity {

    private int banned;
    private int isAdmin;
    private String account;
    private String password;

    public User(int id, int banned, int isAdmin, String account, String password){
        this.id = id;
        this.banned = banned;
        this.isAdmin = isAdmin;
        this.account = account;
        this.password = password;
    }

    public User(int isAdmin, String account, String password){
        this.id = 0;
        this.banned = 0;
        this.isAdmin = isAdmin;
        this.account = account;
        this.password = password;
    }

    @Override
    public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
