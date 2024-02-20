package com.example.gymbuddy;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class GymProgramInfoActivity extends AppCompatActivity {
    public static final String CHANGED = "changed";
    public static final String GYM_PROGRAM_ID = "id";
    public static final String GYM_PROGRAM_NAME = "name";

    protected long gymProgramId;
    protected String gymProgramName;
    protected GymProgram program;

    TextView exerciseCountTextView;
    TextView durationTextView;
    TextView goalText;
    TextView descriptionText;
    ImageView[] difficultyFireImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gym_program_info);

        //Set components
        exerciseCountTextView = findViewById(R.id.exerciseCounter);
        durationTextView = findViewById(R.id.programDuration);
        difficultyFireImages = new ImageView[3];
        difficultyFireImages[0] = findViewById(R.id.flame1);
        difficultyFireImages[1] = findViewById(R.id.flame2);
        difficultyFireImages[2] = findViewById(R.id.flame3);
        goalText = findViewById(R.id.goalText);
        descriptionText = findViewById(R.id.descriptionText);

        Intent intent = getIntent();
        gymProgramId = intent.getLongExtra(GYM_PROGRAM_ID, -1);
        gymProgramName = intent.getStringExtra(GYM_PROGRAM_NAME);

        setActivityTitle();

        setComponents();

        FloatingActionButton addExerciseBtn = findViewById(R.id.add_program);
        addExerciseBtn.setOnClickListener(view -> updateGymProgramInfo());

        FloatingActionButton homeBtn = findViewById(R.id.home_button);
        homeBtn.setOnClickListener(view -> {startHomeActivity();});
    }

    private void setComponents() {
        try (DatabaseHandler dbHelper = new DatabaseHandler(this)) {
            program = dbHelper.getGymProgramByID(gymProgramId);
            goalText.setText(makeProgramGoal(program));
            descriptionText.setText(makeProgramDescription(program));
            durationTextView.setText(program.getDuration());
            exerciseCountTextView.setText(String.valueOf(program.getExerciseCount()));
            for (int i = 0; i< program.getDifficulty(); i++){
                difficultyFireImages[i].setImageResource(R.drawable.redfire);
            }
        }
    }

    private void setActivityTitle(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(gymProgramName + " info");
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private String makeProgramDescription(GymProgram p){
        String description = getResources().getString(R.string.description_unavailable);
        if (p != null && !p.getDescription().equals("")){
            description = p.getDescription();
        }
        return description;
    }

    private String makeProgramGoal(GymProgram p){
        String goal = getResources().getString(R.string.goal_unavailable);
        if (p != null && !p.getGoal().equals("")){
            goal = p.getGoal();
        }
        return goal;
    }

    public void updateGymProgramInfo() {
        Intent i = new Intent(this, AddGymProgramInfoActivity.class);
        i.putExtra("GymProgram", program);
        i.putExtra("edit", true);
        startActivityForResult(i,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            setComponents();
            setActivityTitle();
        }
    }

    public void startHomeActivity(){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Intent out = new Intent();
            out.putExtra(CHANGED, true);
            setResult(RESULT_OK, out);
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}