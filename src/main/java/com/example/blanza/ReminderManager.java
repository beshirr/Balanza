package com.example.blanza;

import io.github.cdimascio.dotenv.Dotenv;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class ReminderManager {
    private final PriorityQueue<Reminder> reminderQueue = new PriorityQueue<>();
    private Thread reminderThread;
    private boolean isRunning = false;

    public ReminderManager() {
        loadRemindersFromDatabase();
    }
    
    /**
     * Creates and saves a new reminder
     *
     * @param reminder the reminder to save
     * @return True if reminder was successfully created, false otherwise
     */
    public boolean addReminder(Reminder reminder) {
        // Validate reminder data
        if (!validateReminderData(reminder)) {
            return false;
        }
        
        // Save to database first to get the generated ID
        ReminderDB.insertReminder(reminder);
        
        // Set the generated ID
        reminder.setId(reminder.getId());
        
        // Add to queue
        reminderQueue.add(reminder);
        
        return true;
    }
    
    /**
     * Validates reminder data according to business rules
     */
    private boolean validateReminderData(Reminder reminder) {
        // Title must be between 3 and 50 characters
        if (reminder.getTitle() == null || reminder.getTitle().length() < 3 || reminder.getTitle().length() > 50) {
            return false;
        }
        
        // Date must be in the future
        return reminder.getTime() != null && !reminder.getTime().isBefore(LocalDateTime.now());
    }
    
    /**
     * Loads all reminders for the current user from database
     */
    private void loadRemindersFromDatabase() {
        reminderQueue.clear();
        List<Reminder> reminders = ReminderDB.getAllReminders();
        reminderQueue.addAll(reminders);
    }

    /**
     * Gets all reminders for the current user
     */
    public List<Reminder> getAllReminders() {
        return new ArrayList<>(reminderQueue);
    }

    /**
     * Main logic for checking and sending reminders
     */
    private void sendReminder() throws InterruptedException {
        while (isRunning) {
            try {
                if (reminderQueue.isEmpty()) {
                    Thread.sleep(1000);
                    continue;
                }

                Reminder nextReminder = reminderQueue.peek();
                if (LocalDateTime.now().isAfter(nextReminder.getTime())) {
                    // Send notification
                    sendNotification(nextReminder);
                    
                    // Remove the reminder from the queue
                    reminderQueue.poll();
                } else {
                    // Sleep until the next reminder is due, but check at least every minute
                    long sleepTime = Math.min(
                        Duration.between(LocalDateTime.now(), nextReminder.getTime()).toMillis(),
                        60000); // 1 minute
                    Thread.sleep(sleepTime);
                }
            } catch (InterruptedException e) {
                System.out.println("Reminder thread interrupted.");
                throw e;
            }
        }
    }
    
    /**
     * Sends a notification for a specific reminder
     */
private void sendNotification(Reminder reminder) {
        // TODO: Send an actual email
        String body = "NOTIFICATION: " + reminder.toString();
        Dotenv dotenv = Dotenv.configure().directory(".").load();
        String email = dotenv.get("EMAIL_SENDER");
        String password = dotenv.get("PASS");
        EmailManager emailManager = new EmailManager(email, password);
        emailManager.sendEmail(UserDB.getUserEmailById(Session.getCurrentUserId()), "Balanza Reminder", body);
    }

    /**
     * Starts the reminder service thread
     */
    public void startReminderService() {
        if (reminderThread != null && reminderThread.isAlive()) {
            return; // Already running
        }
        
        isRunning = true;
        reminderThread = new Thread(() -> {
            try {
                sendReminder();
            } catch (InterruptedException e) {
                System.out.println("Reminder service stopped.");
            }
        });
        reminderThread.setDaemon(true);
        reminderThread.start();
    }
    
    /**
     * Stops the reminder service thread
     */
    public void stopReminderService() {
        isRunning = false;
        if (reminderThread != null) {
            reminderThread.interrupt();
        }
    }
}