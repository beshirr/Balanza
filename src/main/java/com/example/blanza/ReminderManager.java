package com.example.blanza;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.PriorityQueue;

public class ReminderManager {
    private User currentUser;
    private static int nextId = 1;

    public ReminderManager(User currentUser) {
        this.currentUser = currentUser;
    }

    public void setNewReminder(String title, String description, LocalDateTime time, FinancialTask task) {
        currentUser.reminderQueue.add(new Reminder(nextId++, title, description, time, task));
    }

    public void deleteReminder(int id) {
        currentUser.reminderQueue.removeIf(reminder -> reminder.getId() == id);
    }

    public void sendReminder() throws InterruptedException {
        PriorityQueue<Reminder> reminders = currentUser.reminderQueue;
        while (true) {
            try {
                if (reminders.isEmpty()) {
                    Thread.sleep(1000);
                    continue;
                }

                Reminder nextReminder = reminders.peek();
                if (LocalDateTime.now().isAfter(nextReminder.getTime())) {
                    // TODO: Send notification
                    System.out.println("Reminder: " + nextReminder.getTitle());

                    reminders.poll();
                } else {
                    Thread.sleep(Duration.between(LocalDateTime.now(), nextReminder.getTime()).toMillis());
                }
            } catch (InterruptedException e) {
                System.out.println("Thread interrupted.");
                throw e;
            }
        }
    }

    public void startReminderThread() {
        Thread reminderThread = new Thread(() -> {
            try {
                sendReminder();
            } catch (InterruptedException e) {
                System.out.println("Reminder service stopped.");
            }
        });
        reminderThread.setDaemon(true);
        reminderThread.start();
    }
}
