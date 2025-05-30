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
    private final ReminderDB db = new ReminderDB();
    private LocalDateTime lastRefreshTime = LocalDateTime.MIN;
    private static final Duration REFRESH_INTERVAL = Duration.ofSeconds(30); 

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
        
        if (!validateReminderData(reminder)) {
            return false;
        }
        
        
        db.insertToDatabase(reminder);
        
        
        reminder.setId(reminder.getId());
        
        
        reminderQueue.add(reminder);
        
        return true;
    }
    
    /**
     * Validates reminder data according to business rules
     */
    private boolean validateReminderData(Reminder reminder) {
        
        if (reminder.getTitle() == null || reminder.getTitle().length() < 3 || reminder.getTitle().length() > 50) {
            return false;
        }
        
        
        return reminder.getTime() != null && !reminder.getTime().isBefore(LocalDateTime.now());
    }
    
    /**
     * Loads all reminders for the current user from database
     */
    private void loadRemindersFromDatabase() {
        reminderQueue.clear();
        List<Reminder> reminders = db.getAllFromDatabase();
        reminderQueue.addAll(reminders);
        lastRefreshTime = LocalDateTime.now();
    }

    /**
     * Gets all reminders for the current user
     * Will refresh data from database if it hasn't been refreshed recently
     */
    public List<Reminder> getAllReminders() {
        
        if (Duration.between(lastRefreshTime, LocalDateTime.now()).compareTo(REFRESH_INTERVAL) > 0) {
            loadRemindersFromDatabase();
        }
        return new ArrayList<>(reminderQueue);
    }

    /**
     * Main logic for checking and sending reminders
     */
    private void sendReminder() throws InterruptedException {
        while (isRunning) {
            try {
                
                if (Duration.between(lastRefreshTime, LocalDateTime.now()).compareTo(REFRESH_INTERVAL) > 0) {
                    loadRemindersFromDatabase();
                }
                
                if (reminderQueue.isEmpty()) {
                    Thread.sleep(1000);
                    continue;
                }

                Reminder nextReminder = reminderQueue.peek();
                if (LocalDateTime.now().isAfter(nextReminder.getTime())) {
                    
                    sendNotification(nextReminder);
                    
                    
                    reminderQueue.poll();
                } else {
                    
                    long sleepTime = Math.min(
                        Duration.between(LocalDateTime.now(), nextReminder.getTime()).toMillis(),
                        60000); 
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
        String fromEmail = Dotenv.configure().directory(".").load().get("EMAIL_SENDER");
        String password = Dotenv.configure().directory(".").load().get("PASS");
        String subject = "Reminder: " + reminder.getTitle();
        String body = "Don't Forget to Pay for " + reminder.getTitle() + " " + reminder.getDescription() + " on " + reminder.getTime().toString();
        EmailManager emailManager = new EmailManager(fromEmail, password);
        emailManager.sendEmail(UserDB.getUserEmailById(SessionService.getCurrentUserId()), subject, body);
    }

    /**
     * Starts the reminder service thread
     */
    public void startReminderService() {
        if (reminderThread != null && reminderThread.isAlive()) {
            return; 
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
    
    /**
     * Force a refresh of data from the database
     */
    public void refreshData() {
        loadRemindersFromDatabase();
    }
}