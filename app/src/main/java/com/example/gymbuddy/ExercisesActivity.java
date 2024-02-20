package com.example.gymbuddy;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;

import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import androidx.appcompat.app.ActionBar;


import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Objects;

public class ExercisesActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    MainRecyclerAdapter adapter = new MainRecyclerAdapter(this);
    Spinner displaySpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercises);

        recyclerView = findViewById(R.id.main_recycler);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        displaySpinner = findViewById(R.id.select_group_Display);

        int muscleGroup = Objects.requireNonNull(getIntent().getExtras()).getInt(MainActivity.MUSCLE_GROUP);

        setActivityTitle(muscleGroup);

        readExercisesById(muscleGroup, 0);

        setSecondarySpinner(muscleGroup);
        
        FloatingActionButton addExerciseBtn = findViewById(R.id.add_activity);
        addExerciseBtn.setOnClickListener(view -> addExercise(muscleGroup));

        FloatingActionButton homeBtn = findViewById(R.id.home_button);
        homeBtn.setOnClickListener(view -> {startHomeActivity();});

    }

    private void setActivityTitle(int muscleGroup){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(changeActivityName(muscleGroup));
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void readExercisesById(long mainID, int secID) {
        List<Exercise> e;
        try (DatabaseHandler dbHelper = new DatabaseHandler(this)) {
            if (secID == 0)
                e = dbHelper.getExerciseByMainGroup(mainID);
            else
                e = dbHelper.getExerciseByGroup(mainID, secID);
        }

        MainRecyclerAdapter adapter = (MainRecyclerAdapter) recyclerView.getAdapter();
        if (adapter != null) {
            adapter.setExercises(e);
            adapter.notifyDataSetChanged();
        } else {
            System.err.println("Adapter is null");
        }
    }

    private void setSecondarySpinner(int muscleGroup) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                changeSecondarySpinner(muscleGroup),
                android.R.layout.simple_spinner_item
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        displaySpinner.setAdapter(adapter);

        final int mainGroup = muscleGroup;
        displaySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                readExercisesById(mainGroup, position);
            }
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
    }

    public void addExercise(int mGroup) {
        Intent i = new Intent(this, AddExercise.class);
        Bundle b = new Bundle();
        b.putInt(MainActivity.MUSCLE_GROUP, mGroup);
        b.putBoolean("new", true);
        i.putExtras(b);
        startActivity(i);
    }

    @Override
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

    protected int changeSecondarySpinner(int mGroup) {
        int secondaryGroup = 0;
        switch (mGroup) {
            case 1:
                secondaryGroup = R.array.chest_groups;
                break;
            case 2:
                secondaryGroup = R.array.back_groups;
                break;
            case 3:
                secondaryGroup = R.array.shoulders_groups;
                break;
            case 4:
                secondaryGroup = R.array.arms_groups;
                break;
            case 5:
                secondaryGroup = R.array.abs_groups;
                break;
            case 6:
                secondaryGroup = R.array.legs_groups;
                break;
            case 7:
                secondaryGroup = R.array.aerobic_groups;
                break;
        }
        return secondaryGroup;
    }

    public int changeActivityName(int mGroup){
        int actionBarTitle;
        switch (mGroup){
            case 1:
                actionBarTitle = R.string.chest_exercises;
                break;
            case 2:
                actionBarTitle = R.string.back_exercises;
                break;
            case 3:
                actionBarTitle = R.string.shoulders_exercises;
                break;
            case 4:
                actionBarTitle = R.string.arms_exercises;
                break;
            case 5:
                actionBarTitle = R.string.abs_exercises;
                break;
            case 6:
                actionBarTitle = R.string.legs_exercises;
                break;
            case 7:
                actionBarTitle = R.string.aerobic_exercises;
                break;
            default:
                actionBarTitle = R.string.muscle_groups;
        }
        return actionBarTitle;
    }
}