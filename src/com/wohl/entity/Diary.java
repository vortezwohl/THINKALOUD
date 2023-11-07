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
public class Diary extends Entity {

    private int _private;
    private int locked;
    private int reported;
    private int idOfWriter;
    private String title;
    private String content;
    private String datetime;
    private String accountOfWriter;
    private String reportReason;

    public Diary(int id, int _private, int locked, int reported, int idOfWriter, String title, String content, String datetime, String accountOfWriter, String reportReason){
        this.id = id;
        this._private = _private;
        this.locked = locked;
        this.reported = reported;
        this.idOfWriter = idOfWriter;
        this.title = title;
        this.content = content;
        this.datetime = datetime;
        this.accountOfWriter = accountOfWriter;
        this.reportReason = reportReason;
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
