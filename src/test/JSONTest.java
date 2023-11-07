package test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

public class JSONTest {
    public static void main(String[] args) {

        Json json = new Json(2003, "wohl");

        System.out.println(json);
    }
}

@Data
class Json{

    private int myInt;
    private String myString;

    Json(int myInt,String myString){
        this.myInt = myInt;
        this.myString = myString;
    }

    //General toString method for jsonify
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
