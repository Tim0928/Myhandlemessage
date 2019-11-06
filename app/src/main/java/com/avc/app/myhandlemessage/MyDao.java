package com.avc.app.myhandlemessage;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface MyDao {
    @Insert//這個＠代表它會幫我做insert
public void addbook(Book book);
    @Query("select * from lib")
    public List<Book> getbook();//撰寫getuser
    @Delete
    public void deletebook(Book book);
    @Update
    public void updatebook(Book book);
}