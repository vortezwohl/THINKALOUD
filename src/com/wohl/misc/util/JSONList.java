package com.wohl.misc.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wohl.entity.std.Entity;
import lombok.Data;
import java.util.ArrayList;

@Data
public class JSONList<T extends Entity> {

    private String result;
    public JSONList (ArrayList<T> jsonList){
        try {
            result = new ObjectMapper().writeValueAsString(jsonList);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
    @Override
    public String toString(){
        return result;
    }
}