package com.example.gymbuddy;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ExerciseInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_info);

        // Components
        ImageView exerciseInfoPhoto = findViewById(R.id.exerciseInfoPhoto);
        TextView exerciseInfoText = findViewById(R.id.exerciseInfoText);
        FloatingActionButton homeBtn = findViewById(R.id.home_button);

        exerciseInfoPhoto.setVisibility(View.GONE);

        Bundle b = getIntent().getExtras();
        long exerciseID = 0;
        if(b != null) {
            exerciseID = b.getLong("exerciseID");

        }

        Exercise exercise;
        try (DatabaseHandler dbHelper = new DatabaseHandler(this)) {
            exercise = dbHelper.getExerciseByID(exerciseID);

            setActivityTitle(exercise.getName());

            if (exercise.getPhotograph() != 0) {
                exerciseInfoPhoto.setImageResource(exercise.getPhotograph());
                exerciseInfoPhoto.setVisibility(View.VISIBLE);
            }
            if (!exercise.getDescription().equals(""))
                exerciseInfoText.setText(exercise.getDescription());
            else
                exerciseInfoText.setText(R.string.no_info);
        }catch (Exception e){
            setActivityTitle("No available");
            Toast toast = Toast.makeText(this, "Info unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }

        homeBtn.setOnClickListener(view -> {startHomeActivity();});

    }

    public void setActivityTitle(String exerciseName){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(exerciseName + " info");
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void startHomeActivity(){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}