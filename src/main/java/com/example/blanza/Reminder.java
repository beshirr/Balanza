package com.example.blanza;

import java.time.LocalDateTime;

public class Reminder implements Comparable<Reminder> {
    private int id;
    private String title;
    private String description;
    private LocalDateTime time;
    private FinancialTask task;

    public Reminder(int id, String title, String description, LocalDateTime time, FinancialTask task) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.time = time;
        this.task = task;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public LocalDateTime getTime() { return time; }

    @Override
    public int compareTo(Reminder other) {
        return this.time.compareTo(other.time);
    }

    @Override
    public String toString() {
        return "Reminder: " + title + " at " + time;
    }
}
