package com.example.gymbuddy;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class SingleGymProgramActivity extends AppCompatActivity {
    public static final String GYM_PROGRAM_ID = "id";
    public static final String GYM_PROGRAM_NAME = "name";

    protected long gymProgramId;
    protected String gymProgramName;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    MainRecyclerAdapter adapter = new MainRecyclerAdapter(this);

    GymProgram program;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_gym_program);

        Intent intent = getIntent();
        gymProgramId = intent.getIntExtra(GYM_PROGRAM_ID, -1);
        gymProgramName = intent.getStringExtra(GYM_PROGRAM_NAME);

        setActivityTitle();

        recyclerView = findViewById(R.id.gym_program_exercises_recycler);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        FloatingActionButton homeBtn = findViewById(R.id.home_button);
        homeBtn.setOnClickListener(view -> {startHomeActivity();});

        readExercisesById();
    }

private void setActivityTitle(){
    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
        actionBar.setTitle(gymProgramName);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }
}
    private boolean readExercisesById() {
        ArrayList<Exercise> exercises = new ArrayList<>();
        ArrayList<Integer> exerciseIDs;
        try (DatabaseHandler dbHelper = new DatabaseHandler(this)) {
            program = dbHelper.getGymProgramByID(gymProgramId);
            exerciseIDs = program.getExerciseIDs();
            for (int i = 0; i < program.getExerciseCount(); i++){
                exercises.add(dbHelper.getExerciseByID(exerciseIDs.get(i)));
            }

        }

        MainRecyclerAdapter adapter = (MainRecyclerAdapter) recyclerView.getAdapter();
        if (adapter != null) {
            adapter.setExercises(exercises);
            adapter.notifyDataSetChanged();
        } else {
            System.err.println("Adapter is null");
        }
        return !exerciseIDs.isEmpty();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_gym_program, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_gymProgramInfo) {
            Intent i = new Intent(this, GymProgramInfoActivity.class);
            i.putExtra(GymProgramInfoActivity.GYM_PROGRAM_ID, gymProgramId);
            i.putExtra(GymProgramInfoActivity.GYM_PROGRAM_NAME, gymProgramName);
            startActivity(i);
            return true;
        }
        if (id == R.id.action_edit_info) {
            Intent i = new Intent(this, AddGymProgramInfoActivity.class);
            i.putExtra("GymProgram", program);
            i.putExtra("edit", true);
            startActivityForResult(i,1);
            return true;
        }
        if (id == R.id.action_edit_exercises) {
            Intent i = new Intent(this, AddGymProgramExercisesActivity.class);
            i.putExtra("GymProgramInfo", program);
            i.putExtra("edit", true);
            startActivityForResult(i,1);
            return true;
        }
        if (id == R.id.action_delete) {
            try (DatabaseHandler dbHelper = new DatabaseHandler(this)) {
                dbHelper.deleteGymProgram(gymProgramId);
                Intent outIntent = new Intent(this, GymProgramsActivity.class);
                Toast toast = Toast.makeText(this, "Program deleted", Toast.LENGTH_SHORT);
                toast.show();
                startActivity(outIntent);
            }catch (Exception e){
                Toast toast = Toast.makeText(this, "Unable to delete program", Toast.LENGTH_SHORT);
                toast.show();
            }
            return true;
        }
        if (id == android.R.id.home) {
            Intent out = new Intent();
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            readExercisesById();
        }
    }


    public void startHomeActivity(){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}