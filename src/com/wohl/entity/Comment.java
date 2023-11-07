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
public class Comment extends Entity {

    private int idOfDiary;
    private int idOfAnotherComment;
    private int reported;
    private int idOfWriter;
    private String accountOfWriter;
    private String content;
    private String datetime;

    public Comment(int idOfDiary, int idOfAnotherComment, int id, int reported, int idOfWriter, String accountOfWriter, String content, String datetime) {
        this.idOfDiary = idOfDiary;
        this.idOfAnotherComment = idOfAnotherComment;
        this.id = id;
        this.reported = reported;
        this.idOfWriter = idOfWriter;
        this.accountOfWriter = accountOfWriter;
        this.content = content;
        this.datetime = datetime;
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
