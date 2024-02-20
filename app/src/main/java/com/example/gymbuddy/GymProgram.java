package com.example.gymbuddy;

import java.io.Serializable;
import java.util.ArrayList;

public class GymProgram implements Serializable {

    private int gymProgramID;
    private String name;
    private String description;
    private String goal;
    private int durationInMinutes;
    private int difficulty;

    private ArrayList<Integer> exerciseIDs;

    public GymProgram(){
        gymProgramID = 0;
        name = "";
    }

    public GymProgram(int id, String name, ArrayList<Integer> exercises){
        this.gymProgramID = id;
        this.name = name;
        this.exerciseIDs = exercises;
    }

    public GymProgram(String name, String description, String goal, int duration, int difficulty){
        this.gymProgramID = 0;
        this.name = name;
        this.description = description;
        this.goal = goal;
        this.durationInMinutes = duration;
        this.difficulty = difficulty;
        this.exerciseIDs = new ArrayList<Integer>();
    }

    public GymProgram(int id, String name, String exercises, String description, String goal, int duration, int difficulty){
        this.gymProgramID = id;
        this.name = name;
        this.description = description;
        this.goal = goal;
        this.durationInMinutes = duration;
        this.difficulty = difficulty;
        this.exerciseIDs = new ArrayList<Integer>();
        retrieveExerciseFromString(exercises);

    }

    public String storeExercisesToString(){
        String exercisesString = "";
        for(Integer exID : exerciseIDs)
        {
            exercisesString += exID;
            exercisesString += ",";
        }
        return exercisesString;
    }

    public void retrieveExerciseFromString(String exercisesString){
        if (!exercisesString.equals("")) {
            String[] numericSubstrings = exercisesString.split(",");
            for (String number : numericSubstrings) {
                this.exerciseIDs.add(Integer.parseInt(number));
            }
        }
    }

    public int getExerciseCount(){
        return exerciseIDs.size();
    }
    public int getGymProgramID() {
        return gymProgramID;
    }
    public void setGymProgramID(int gymProgramID) {
        this.gymProgramID = gymProgramID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }


    public String getGoal() {
        return goal;
    }

    public String getDuration() {
        String d = String.valueOf(durationInMinutes);
        return d + " min";
    }
    public int getDifficulty() {
        return difficulty;
    }

    public ArrayList<Integer> getExerciseIDs(){
        ArrayList<Integer> ex = exerciseIDs;
        return (exerciseIDs);
    }

    public void setExerciseIDs(ArrayList<Integer> exerciseIDs) {
        this.exerciseIDs = exerciseIDs;
    }
}

