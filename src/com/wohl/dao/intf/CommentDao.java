package com.wohl.dao.intf;

import com.wohl.entity.Comment;
import com.wohl.entity.Diary;
import java.util.ArrayList;

public interface CommentDao {
    public abstract boolean addComment(Diary diary, Comment comment);
    public abstract boolean removeComment(Comment comment);
    public abstract ArrayList<Comment> listAllFrom(Diary diary);
    public abstract boolean removeCommentsUnderDiary(Diary diary);
}
