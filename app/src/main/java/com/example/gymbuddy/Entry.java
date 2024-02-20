package com.example.gymbuddy;

public class Entry {
    private long entry_id;
    private String date;
    private long exercise_id;
    private String weight;
    private String sets;
    private  String reps;
    private String duration;
    private String rest_time;
    private String pace;
    private String comments;

    private String exerciseName;

    public Entry(long entry_id, String date, long exercise_id, String weight, String sets, String reps, String duration, String rest_time, String pace, String comments) {
        this.entry_id = entry_id;
        this.date = date;
        this.exercise_id = exercise_id;
        this.weight = weight;
        this.sets = sets;
        this.reps = reps;
        this.duration = duration;
        this.rest_time = rest_time;
        this.pace = pace;
        this.comments = comments;
    }
    public Entry(long entry_id, String date, long exercise_id, String weight, String sets, String reps, String duration, String rest_time, String pace, String comments, String exerciseName) {
        this.entry_id = entry_id;
        this.date = date;
        this.exercise_id = exercise_id;
        this.weight = weight;
        this.sets = sets;
        this.reps = reps;
        this.duration = duration;
        this.rest_time = rest_time;
        this.pace = pace;
        this.comments = comments;
        this.exerciseName = exerciseName;
    }

    public long getEntry_id() {
        return entry_id;
    }

    public String getDate() {
        return date;
    }

    public long getExerciseId() {
        return exercise_id;
    }

    public void setExercise(long exercise) {
        this.exercise_id = exercise_id;
    }

    public String getWeight() {
        return weight;
    }
    public String getSets() {
        return sets;
    }
    public String getReps() {
        return reps;
    }
    public String getDuration() {
        return duration;
    }
    public String getRest_time() {
        return rest_time;
    }
    public String getPace() {
        return pace;
    }
    public String getComments() {
        return comments;
    }

    public String getExerciseName() {
        return exerciseName;
    }
}
