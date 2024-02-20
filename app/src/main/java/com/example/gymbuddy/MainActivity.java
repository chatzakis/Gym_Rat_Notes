package com.example.gymbuddy;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    public static final String MUSCLE_GROUP = "muscleGroup";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setMainButtons();

        addRecordsToDatabase();


    }

    private void setMainButtons(){
        //Set buttons
        Button programsBtn = findViewById(R.id.programsBtn);
        programsBtn.setOnClickListener(v -> goToPrograms());

        Button lastEntriesBtn = findViewById(R.id.lastEntriesBtn);
        lastEntriesBtn.setOnClickListener(v -> goToLastEntries());

        // Set Image Buttons
        ImageButton chestButton =  findViewById(R.id.chestButton);
        chestButton.setOnClickListener(v -> goToExercises(1));

        ImageButton backButton =  findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> goToExercises(2));

        ImageButton shouldersButton =  findViewById(R.id.shouldersButton);
        shouldersButton.setOnClickListener(v -> goToExercises(3));

        ImageButton armsButton =  findViewById(R.id.armsButton);
        armsButton.setOnClickListener(v -> goToExercises(4));

        ImageButton absButton =  findViewById(R.id.absButton);
        absButton.setOnClickListener(v -> goToExercises(5));

        ImageButton legsButton =  findViewById(R.id.legsButton);
        legsButton.setOnClickListener(v -> goToExercises(6));

        ImageButton aerobicButton =  findViewById(R.id.aerobicButton);
        aerobicButton.setOnClickListener(v -> goToExercises(7));
    }

    private void goToPrograms() {
        Intent i = new Intent(this, GymProgramsActivity.class);
        startActivity(i);
    }

    private void goToLastEntries() {
        Intent i = new Intent(this, LastEntriesActivity.class);
        startActivity(i);
    }

    private void goToExercises(int muscleGroup) {
        Intent i = new Intent(this, ExercisesActivity.class);
        i.putExtra(MUSCLE_GROUP, muscleGroup);
        startActivity(i);
    }

    protected void addRecordsToDatabase(){
        try (DatabaseHandler dbHelper = new DatabaseHandler(this)) {

            if (dbHelper.checkTableEmpty("Exercise")){
                RecordsInitiatior recordsInitiatior = new RecordsInitiatior();

                ArrayList<Exercise> recs = recordsInitiatior.getExerciseRecords();
                for (Exercise e : recs) {
                    dbHelper.addExerciseWithoutMessage(e);
                }
                Toast toast = Toast.makeText(this, "Exercises installation completed", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

}