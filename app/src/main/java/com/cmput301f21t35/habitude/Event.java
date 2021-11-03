package com.cmput301f21t35.habitude;

/*
Class for the habit event

TO DO:
may need unique id for event, id is included as a variable just in case
have to find a way for habit to store events
 */
public class Event {
    private String habitName;
    private String habitComment;
    // private String image; for image implementation later

    Event() {}

    Event(String habitName, String habitComment) {
        this.habitName = habitName;
        this.habitComment = habitComment;
    }

    // getters and setters

    public String getHabitName() {
        return habitName;
    }

    public void setHabitName(String habitName) {
        this.habitName = habitName;
    }

    public String getHabitComment() {
        return habitComment;
    }

    public void setHabitComment(String habitComment) {
        this.habitComment = habitComment;
    }
}