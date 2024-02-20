package com.example.gymbuddy;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class AddEntryActivity extends AppCompatActivity {
    public static final String ENTRY_ID = "id";
    public static final String EXERCISE_ID= "Exercise_ID";
    public static final String ENTRY_DATE = "Date";
    public static final String ENTRY_WEIGHT = "Weight";
    public static final String ENTRY_REPS = "Reps";
    public static final String ENTRY_SETS= "Sets";
    public static final String ENTRY_DURATION = "Duration";
    public static final String ENTRY_PACE = "Pace";
    public static final String ENTRY_REST= "Rest";
    public static final String ENTRY_COMMENTS = "Comments";
    private long exercise_id;
    private String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_entry);

        TextView weightTV = findViewById(R.id.add_weight);
        TextView repsTV = findViewById(R.id.add_reps);
        TextView setsTV = findViewById(R.id.add_sets);
        TextView commentsTV = findViewById(R.id.add_comments);
        TextView durationTV = findViewById(R.id.add_duration);
        TextView paceTV = findViewById(R.id.add_pace);
        TextView restTV = findViewById(R.id.add_rest);
        FloatingActionButton updateEntryBtn = findViewById(R.id.update_entry);
        FloatingActionButton homeBtn = findViewById(R.id.home_button);


        try (DatabaseHandler dbHelper = new DatabaseHandler(this)) {

            Intent intent = getIntent();
            long id = intent.getLongExtra(ENTRY_ID, -1);
            exercise_id = intent.getLongExtra(EXERCISE_ID, -1);
            int actionBarTitle = 0;

            if (id == -1) {
                actionBarTitle = R.string.new_entry;
                updateEntryBtn.setImageResource(android.R.drawable.ic_menu_save);
            } else {
                actionBarTitle = R.string.edit_entry;
                date = intent.getStringExtra(ENTRY_DATE);
                String weight = intent.getStringExtra(ENTRY_WEIGHT);
                String reps = intent.getStringExtra(ENTRY_REPS);
                String sets = intent.getStringExtra(ENTRY_SETS);
                String duration = intent.getStringExtra(ENTRY_DURATION);
                String pace = intent.getStringExtra(ENTRY_PACE);
                String rest = intent.getStringExtra(ENTRY_REST);
                String comments = intent.getStringExtra(ENTRY_COMMENTS);
                weightTV.setText(weight);
                repsTV.setText(reps);
                setsTV.setText(sets);
                durationTV.setText(duration);
                paceTV.setText(pace);
                restTV.setText(rest);
                commentsTV.setText(comments);
            }

            setActivityTitle(actionBarTitle);


            updateEntryBtn.setOnClickListener(view -> {
                String weight = weightTV.getText().toString();
                String reps = repsTV.getText().toString();
                String sets = setsTV.getText().toString();
                String duration = durationTV.getText().toString();
                String pace = paceTV.getText().toString();
                String rest = restTV.getText().toString();
                String comments = commentsTV.getText().toString();
                Entry e = new Entry(id, date, exercise_id, weight, sets, reps, duration, rest, pace, comments);
                if (id == -1) {
                    dbHelper.addEntry(e);
                } else {
                    dbHelper.updateEntry(e);
                }
                Intent out = new Intent();
                setResult(RESULT_OK, out);
                Snackbar.make(view, "Entry Added/Changed", Snackbar.LENGTH_LONG).show();
                finish();
            });

        }catch(Exception e){
            Toast toast = Toast.makeText(this, "Unable to access database", Toast.LENGTH_SHORT);
            toast.show();
        }

        homeBtn.setOnClickListener(view -> {startHomeActivity();});
    }

    private void setActivityTitle(int actionBarTitle){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(actionBarTitle);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
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
}