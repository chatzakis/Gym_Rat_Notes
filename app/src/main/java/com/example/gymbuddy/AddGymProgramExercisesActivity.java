package com.example.gymbuddy;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.Spinner;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class AddGymProgramExercisesActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ProgramExercisesRecyclerAdapter adapter = new ProgramExercisesRecyclerAdapter(this);
    Spinner muscleGroupSpinner;

    GymProgram program;

    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_gym_program_exercises);

        FloatingActionButton updateGymProgram = findViewById(R.id.addGymProgramBtn);
        FloatingActionButton homeBtn = findViewById(R.id.home_button);

        i = getIntent();
        program = (GymProgram) getIntent().getSerializableExtra("GymProgramInfo");

        setActivityTitle(program);

        muscleGroupSpinner = findViewById(R.id.selectMuscleGroup);

        recyclerView = findViewById(R.id.gymProgramExercisesAdapter);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        if (checkEditCondition(i))
            retrieveProgramExercises();

        setSpinner();

        updateGymProgram.setOnClickListener(view -> {

            if (adapter.hasSelections()) {
                assert program != null;
                program.setExerciseIDs(adapter.getSelectedExercisesIds());
                if (checkEditCondition(i))
                    updateGymProgramExercises();
                else
                    addNewGymProgram();
            }else{
                Snackbar.make(view, "Unable to update program", Snackbar.LENGTH_LONG).show();
            }
        });

        readExercises();

        homeBtn.setOnClickListener(view -> {startHomeActivity();});
    }

    private void addNewGymProgram() {
        try (DatabaseHandler dbHelper = new DatabaseHandler(this)) {
            String result = dbHelper.addGymProgram(program);
            Toast toast = Toast.makeText(this, result, Toast.LENGTH_SHORT);
            toast.show();
            goToGymProgramsActivity();
        }catch (Exception e){
            Toast toast = Toast.makeText(this, "Unable to access database", Toast.LENGTH_SHORT);
            toast.show();
        }

    }

    private void updateGymProgramExercises() {
        try (DatabaseHandler dbHelper = new DatabaseHandler(this)) {
            dbHelper.updateGymProgram(program);
            Toast toast = Toast.makeText(this, "Gym program exercises updated", Toast.LENGTH_SHORT);
            toast.show();
            Intent out = new Intent();
            setResult(RESULT_OK, out);
            finish();
        }catch (Exception e){
            Toast toast = Toast.makeText(this, "Unable to access database", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void goToGymProgramsActivity(){
        Intent outIntent = new Intent(this, GymProgramsActivity.class);
        startActivity(outIntent);
    }

    private void retrieveProgramExercises() {
        adapter.setSelectedExercisesIds(program.getExerciseIDs());
    }

    @SuppressLint("NotifyDataSetChanged")
    private void readExercises() {
        List<Exercise> e;
        try (DatabaseHandler dbHelper = new DatabaseHandler(this)) {
            e = dbHelper.getExercises();
        }
        createExercisesRecyclerAdapter(e);
    }

    private void readExercisesById(long mainID) {
        List<Exercise> e;
        try (DatabaseHandler dbHelper = new DatabaseHandler(this)) {
            e = dbHelper.getExerciseByMainGroup(mainID);
        }
        createExercisesRecyclerAdapter(e);
    }

    private void createExercisesRecyclerAdapter(List<Exercise> e) {
        ProgramExercisesRecyclerAdapter adapter = (ProgramExercisesRecyclerAdapter) recyclerView.getAdapter();
        if (adapter != null) {
            adapter.setExercises(e);
            adapter.notifyDataSetChanged();
        } else {
            System.err.println("Adapter is null");
        }
    }

    private void setActivityTitle(GymProgram program){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getActionBarName(program));
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private String getActionBarName(GymProgram program){
        String actionBarTitle = getResources().getString(R.string.add_exercises_to_program);
        if(program.getName() != null){
             actionBarTitle= "Add Exercises to " + program.getName();
        }
        return actionBarTitle;
    }

    private boolean checkEditCondition(Intent i){
        if (i != null)
            return getIntent().getBooleanExtra("edit",false);
        return false;
    }

    private void setSpinner() {
        muscleGroupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0)
                    readExercises();
                else
                    readExercisesById(position);
            }
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_program_exercise, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        int id = item.getItemId();
        if (id == R.id.action_info) {
            Toast toast = Toast.makeText(this, R.string.add_program_exercises_instruction_analytical, Toast.LENGTH_LONG);
            toast.show();
        }
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
