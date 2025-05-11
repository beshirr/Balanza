package com.example.blanza;

import java.time.LocalDateTime;
import java.util.Objects;

public class Reminder implements Comparable<Reminder> {
    private int id;
    private int current_user_id;
    private String title;
    private String description;
    private LocalDateTime time;
    private Integer task_id;
    private FinancialTask task;

    public Reminder(int id, int current_user_id, String title, String description, LocalDateTime time, Integer task_id, FinancialTask task) {
        this.id = id;
        this.current_user_id = current_user_id;
        this.title = title;
        this.description = description;
        this.time = time;
        this.task_id = task_id;
        this.task = task;
    }

    public Reminder(int current_user_id, String title, String description, LocalDateTime time, Integer task_id) {
        this.current_user_id = current_user_id;
        this.title = title;
        this.description = description;
        this.time = time;
        this.task_id = task_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCurrent_user_id() {
        return current_user_id;
    }

    public void setCurrent_user_id(int current_user_id) {
        this.current_user_id = current_user_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public Integer getTask_id() {
        return task_id;
    }

    public void setTask_id(Integer task_id) {
        this.task_id = task_id;
    }

    public FinancialTask getTask() {
        return task;
    }

    public void setTask(FinancialTask task) {
        this.task = task;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reminder reminder = (Reminder) o;
        return id == reminder.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return title + " - " + time;
    }

    @Override
    public int compareTo(Reminder other) {
        return this.time.compareTo(other.time);
    }
}