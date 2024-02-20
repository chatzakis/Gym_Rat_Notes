package com.example.gymbuddy;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class EntriesActivity extends AppCompatActivity {
    public static final String EXERCISE_ID = "id";
    public static final String EXERCISE_NAME = "name";

    protected long exerciseId;

    protected int muscleGroup;
    protected String exerciseName;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    EntriesRecyclerAdapter adapter = new EntriesRecyclerAdapter(this);
    ActionBar actionBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entries);

        TextView instructionText = findViewById(R.id.addEntryInstruction);

        Intent intent = getIntent();
        exerciseId = intent.getLongExtra(EXERCISE_ID, -1);
        exerciseName = intent.getStringExtra(EXERCISE_NAME);
        muscleGroup = (int)intent.getLongExtra(MainActivity.MUSCLE_GROUP, 0);

        setActionBar();

        FloatingActionButton fab = findViewById(R.id.add_entry);
        FloatingActionButton homeBtn = findViewById(R.id.home_button);
        fab.setOnClickListener(view -> addEntry(exerciseId));
        homeBtn.setOnClickListener(view -> {startHomeActivity();});


        recyclerView = findViewById(R.id.entry_recycler);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        if(!readEntries())
            instructionText.setVisibility(View.VISIBLE);
    }

    private void setActionBar(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String actionBarTitle = exerciseName + " " + getString(R.string.title_activity_entries);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(actionBarTitle);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public void addEntry(long id) {
        Intent i = new Intent(this, AddEntryActivity.class);
        i.putExtra(AddEntryActivity.EXERCISE_ID,id);
        startActivityForResult(i,1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_exercise, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_info) {
            Intent i = new Intent(this, ExerciseInfo.class);
            Bundle b = new Bundle();
            b.putLong("exerciseID", exerciseId); //Your id
            i.putExtras(b);
            startActivity(i);
            return true;
        }
        if (id == R.id.action_edit) {
            Intent i = new Intent(this, AddExercise.class);
            Bundle b = new Bundle();
            b.putLong("exerciseID", exerciseId);
            b.putBoolean("edit", true);
            i.putExtras(b);
            startActivity(i);
            return true;
        }
        if (id == R.id.action_delete) {
            try (DatabaseHandler dbHelper = new DatabaseHandler(this)) {
                dbHelper.deleteExercise(exerciseId);
                Toast toast = Toast.makeText(this, "Exercise deleted", Toast.LENGTH_SHORT);
                toast.show();
                goToExercisesActivity(muscleGroup);
            }catch (Exception e){
                Toast toast = Toast.makeText(this, "Unable to delete exercise", Toast.LENGTH_SHORT);
                toast.show();
            }
            return true;
        }
        if (id == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private boolean readEntries() {
        List<Entry> e;
        try (DatabaseHandler dbHelper = new DatabaseHandler(this)) {
            e = dbHelper.getRelevantEntries(exerciseId);
        }

        EntriesRecyclerAdapter adapter = (EntriesRecyclerAdapter) recyclerView.getAdapter();
        if (adapter != null) {
            adapter.setEntries(e);
            adapter.notifyDataSetChanged();
        } else {
            System.err.println("Adapter is null");
        }
        return !e.isEmpty();
    }
    private void goToExercisesActivity(int group) {
        Intent out = new Intent(this, ExercisesActivity.class);
        out.putExtra(MainActivity.MUSCLE_GROUP, group);
        startActivity(out);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            readEntries();
        }
    }

    public void startHomeActivity(){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}