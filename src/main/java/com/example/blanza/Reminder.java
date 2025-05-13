package com.example.blanza;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a reminder in the Balanza personal finance application.
 * <p>
 * The Reminder class models notifications that alert users about upcoming
 * financial events, deadlines, or tasks. Reminders can be:
 * <ul>
 *   <li>Stand-alone reminders for general financial actions</li>
 *   <li>Task-associated reminders that link to a specific FinancialTask</li>
 * </ul>
 * <p>
 * Each reminder has a scheduled time when it should be displayed to the user
 * and includes descriptive information to help users understand the required action.
 * Reminders are tied to specific users to ensure privacy and proper notification routing.
 * <p>
 * This class implements Comparable to allow sorting reminders chronologically,
 * which is useful for displaying upcoming reminders in order of urgency.
 *
 * @see FinancialTask
 */
public class Reminder implements Comparable<Reminder> {
    /** Unique identifier for the reminder */
    private int id;
    
    /** ID of the user who owns this reminder */
    private int current_user_id;
    
    /** Short descriptive title of the reminder */
    private String title;
    
    /** Longer description with details about the reminder */
    private String description;
    
    /** Date and time when the reminder should be triggered */
    private LocalDateTime time;
    
    /** Optional ID of an associated financial task (null if not associated with a task) */
    private Integer task_id;
    
    /** Optional reference to the associated financial task object (null if not associated) */
    private FinancialTask task;

    /**
     * Creates a complete reminder with all fields including ID and task reference.
     * <p>
     * This constructor is typically used when loading existing reminders from the database
     * or when a reminder needs to be created with a known ID.
     *
     * @param id The unique identifier for this reminder
     * @param current_user_id The ID of the user who owns this reminder
     * @param title A short descriptive title for the reminder
     * @param description A longer explanation of the reminder's purpose
     * @param time The date and time when the reminder should be triggered
     * @param task_id The ID of any associated financial task (can be null)
     * @param task Reference to the associated financial task object (can be null)
     */
    public Reminder(int id, int current_user_id, String title, String description, LocalDateTime time, Integer task_id, FinancialTask task) {
        this.id = id;
        this.current_user_id = current_user_id;
        this.title = title;
        this.description = description;
        this.time = time;
        this.task_id = task_id;
        this.task = task;
    }

    /**
     * Creates a new reminder without an ID or task reference.
     * <p>
     * This constructor is typically used when creating a new reminder that
     * hasn't been saved to the database yet. The ID will be assigned when
     * the reminder is persisted.
     *
     * @param current_user_id The ID of the user who owns this reminder
     * @param title A short descriptive title for the reminder
     * @param description A longer explanation of the reminder's purpose
     * @param time The date and time when the reminder should be triggered
     * @param task_id The ID of any associated financial task (can be null)
     */
    public Reminder(int current_user_id, String title, String description, LocalDateTime time, Integer task_id) {
        this.current_user_id = current_user_id;
        this.title = title;
        this.description = description;
        this.time = time;
        this.task_id = task_id;
    }

    /**
     * Returns the unique identifier of this reminder.
     *
     * @return The reminder's ID
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the unique identifier for this reminder.
     * <p>
     * This is typically called after a new reminder has been saved to the database
     * and assigned an ID.
     *
     * @param id The new ID to assign to this reminder
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the ID of the user who owns this reminder.
     *
     * @return The ID of the user associated with this reminder
     */
    public int getCurrent_user_id() {
        return current_user_id;
    }

    /**
     * Sets the ID of the user who owns this reminder.
     * <p>
     * This method allows changing the reminder's ownership, which might be
     * useful when implementing features like reminder sharing or transfer.
     *
     * @param current_user_id The ID of the user who should own this reminder
     */
    public void setCurrent_user_id(int current_user_id) {
        this.current_user_id = current_user_id;
    }

    /**
     * Returns the title of this reminder.
     * <p>
     * The title provides a brief description of what the reminder is about,
     * suitable for display in lists or notification headers.
     *
     * @return The reminder's title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of this reminder.
     *
     * @param title The new title for this reminder
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Returns the detailed description of this reminder.
     * <p>
     * The description provides more comprehensive information about the 
     * reminder's purpose and any actions the user should take.
     *
     * @return The reminder's description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the detailed description of this reminder.
     *
     * @param description The new description for this reminder
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns the date and time when this reminder should be triggered.
     * <p>
     * This time is used to determine when notifications should be shown to the user
     * and for sorting reminders chronologically.
     *
     * @return The LocalDateTime when the reminder should be triggered
     */
    public LocalDateTime getTime() {
        return time;
    }

    /**
     * Sets the date and time when this reminder should be triggered.
     *
     * @param time The new LocalDateTime for this reminder
     */
    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    /**
     * Returns the ID of any associated financial task.
     * <p>
     * This ID links the reminder to a specific financial task. If the reminder
     * is not associated with a task, this method returns null.
     *
     * @return The ID of the associated task, or null if there is no associated task
     */
    public Integer getTask_id() {
        return task_id;
    }

    /**
     * Sets the ID of the associated financial task.
     * <p>
     * This method allows changing the task association or removing it by setting to null.
     *
     * @param task_id The ID of the task to associate, or null to remove the association
     */
    public void setTask_id(Integer task_id) {
        this.task_id = task_id;
    }

    /**
     * Returns the associated financial task object.
     * <p>
     * This provides direct access to the associated task and its properties.
     * Returns null if no task is associated with this reminder.
     *
     * @return The associated FinancialTask object, or null if there is no associated task
     */
    public FinancialTask getTask() {
        return task;
    }

    /**
     * Sets the associated financial task object.
     * <p>
     * This method allows setting or changing the task reference.
     * When setting a task, it's recommended to also update the task_id field
     * to maintain consistency.
     *
     * @param task The FinancialTask to associate with this reminder, or null to remove association
     */
    public void setTask(FinancialTask task) {
        this.task = task;
    }

    /**
     * Determines if this reminder is equal to another object.
     * <p>
     * Two reminders are considered equal if they have the same ID, regardless
     * of other property values. This implementation supports proper functioning
     * in collections like HashSet and HashMap.
     *
     * @param o The object to compare with this reminder
     * @return true if the given object is a Reminder with the same ID, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reminder reminder = (Reminder) o;
        return id == reminder.id;
    }

    /**
     * Generates a hash code for this reminder.
     * <p>
     * The hash code is based solely on the reminder's ID to match the equals implementation.
     * This ensures proper behavior when using reminders in hash-based collections.
     *
     * @return A hash code value for this reminder
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /**
     * Returns a string representation of this reminder.
     * <p>
     * The string includes the reminder's title and scheduled time, making it
     * suitable for display in lists, logs, or debug output.
     *
     * @return A string containing the reminder's title and time
     */
    @Override
    public String toString() {
        return title + " - " + time;
    }

    /**
     * Compares this reminder to another reminder for ordering.
     * <p>
     * Reminders are ordered chronologically based on their scheduled time.
     * This allows collections of reminders to be sorted by when they should be triggered,
     * with earlier reminders coming first.
     *
     * @param other The reminder to compare with this one
     * @return a negative integer if this reminder is earlier, 
     *         zero if they're at the same time, or 
     *         a positive integer if this reminder is later
     */
    @Override
    public int compareTo(Reminder other) {
        return this.time.compareTo(other.time);
    }
}