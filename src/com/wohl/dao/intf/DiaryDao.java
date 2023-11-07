package com.wohl.dao.intf;

import com.wohl.entity.Diary;
import com.wohl.entity.User;
import java.util.ArrayList;

public interface DiaryDao {
    public abstract boolean addDiary(Diary diary);
    public abstract boolean removeDiary(Diary diary);
    public abstract boolean editDiary(Diary diary, String newTitle, String newContent, String newDatetime, int newPrivacy);
    public abstract boolean report(Diary diary, String reason);
    public abstract boolean lockDiary(Diary diary);
    public abstract boolean unlockDiary(Diary diary);
    public abstract ArrayList<Diary> listAll();
    public abstract ArrayList<Diary> listAllFromUser(User user);
    public abstract ArrayList<Diary> listAllFromPublicExcludeUser(User user);
    public abstract Diary fetchDiaryById(int id);
}
