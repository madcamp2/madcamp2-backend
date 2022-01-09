package com.example.everytask.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserToDo {
    private String user_name;
    private int id;
    private int user_id;
    private int course_id;
    private String contents;
    private boolean done;
    private String date;
    private ArrayList<SimpleUserObject> reactionList;
}
