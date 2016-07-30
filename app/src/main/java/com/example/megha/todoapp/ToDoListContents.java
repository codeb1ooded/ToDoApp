package com.example.megha.todoapp;

import java.io.Serializable;

public class ToDoListContents implements Serializable {
    private int id;
    String title;
    String date;
    String content;
    int color;
    private static int count = 0;

    ToDoListContents(String title, String date, String content, int color){
        this.title = title;
        this.date = date;
        this.content = content;
        this.id = ++count;
        this.color = color;
    }

    int getID(){
        return id;
    }

    void setID(int id){
        this.id = id;
    }

}
