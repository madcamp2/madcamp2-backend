package com.example.everytask.model.dto;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskView {
    private int id;
    private int user_id;
    private int course_id;
    private String contents;
    private boolean done;
    private String date;
    private String course_name;
    private int task_likes;
}
