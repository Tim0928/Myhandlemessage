package com.avc.app.myhandlemessage;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "lib")//把table name修改  資料庫建立
public  class Book {

    @PrimaryKey//鑰匙
    private int id;
    @ColumnInfo(name="book_name")//欄位資訊 設定名子為user_name
    private String book_name;
    @ColumnInfo(name = "book_conext")
    private String book_conext;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBook_name() {
        return book_name;
    }

    public void setBook_name(String book_name) {
        this.book_name = book_name;
    }

    public String getBook_conext() {
        return book_conext;
    }

    public void setBook_conext(String book_conext) {
        this.book_conext = book_conext;
    }
}

