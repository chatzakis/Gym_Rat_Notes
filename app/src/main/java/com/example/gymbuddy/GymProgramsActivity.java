package com.example.gymbuddy;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Collections;
import java.util.List;

public class GymProgramsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    GymProgramsRecyclingAdapter adapter = new GymProgramsRecyclingAdapter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gym_programs);

        TextView instructionText = findViewById(R.id.addProgramInstruction);

        setActivityTitle();

        recyclerView = findViewById(R.id.gym_program_recycler);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);


        FloatingActionButton addExerciseBtn = findViewById(R.id.add_program);
        addExerciseBtn.setOnClickListener(view -> addGymProgram());

        FloatingActionButton homeBtn = findViewById(R.id.home_button);
        homeBtn.setOnClickListener(view -> {startHomeActivity();});

        // if function returns entries
        if(!readPrograms())
            instructionText.setVisibility(View.VISIBLE);
    }

    private void setActivityTitle(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.gym_programs);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public void addGymProgram() {
        Intent i = new Intent(this, AddGymProgramInfoActivity.class);
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

    @SuppressLint("NotifyDataSetChanged")
    private boolean readPrograms() {
        List<GymProgram> programs;
        try (DatabaseHandler dbHelper = new DatabaseHandler(this)) {
            programs = dbHelper.getGymPrograms();
        }
        // Sort Exercises
        Collections.sort(programs, (p1, p2) -> p1.getName().compareToIgnoreCase(p2.getName()));
        System.out.println("Exercises: " + programs.size());

        GymProgramsRecyclingAdapter adapter;
        adapter = (GymProgramsRecyclingAdapter) recyclerView.getAdapter();
        if (adapter != null) {
            adapter.setGymPrograms(programs);
            adapter.notifyDataSetChanged();
        } else {
            System.err.println("Adapter is null");
        }
        return !programs.isEmpty();
    }

    public void startHomeActivity(){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}