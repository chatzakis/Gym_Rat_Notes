package com.example.gymbuddy;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AddGymProgramInfoActivity extends AppCompatActivity {
    public static final int DEFAULT_DURATION = 30;
    public static final int DURATION_LOWER_BOUND = 1;
    public static final int DEFAULT_DIFFICULTY = 1;
    public static final int DIFFICULTY_LOWER_BOUND = 0;
    public static final int DIFFICULTY_UPPER_BOUND = 3;

    private EditText gymProgramName;
    private EditText gymProgramGoal;
    private EditText gymProgramDuration;
    private EditText gymProgramDifficulty;
    private EditText gymProgramDescription;
    private FloatingActionButton storeGymProgramInfoBtn;
    private TextView addExerciseInstruction;

    private String name;
    private String goal;
    private int duration;
    private int difficulty;
    private String description;

    private GymProgram program;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_gym_program_info);

        //Components
        gymProgramName = findViewById(R.id.editTextProgramName);
        gymProgramGoal = findViewById(R.id.editTextProgramGoal);
        gymProgramDuration = findViewById(R.id.editDurationNumber);
        gymProgramDifficulty = findViewById(R.id.editDifficultyNumber);
        gymProgramDescription = findViewById(R.id.editTextProgramDescription);

        storeGymProgramInfoBtn = findViewById(R.id.addGymProgramBtn);
        addExerciseInstruction = findViewById(R.id.addExercisesInstruction);


        setActivityTitle();
        
        Bundle bIn = getIntent().getExtras();

        if (checkEditCondition(bIn))
            loadGymProgramInfo();

        storeGymProgramInfoBtn.setOnClickListener(view -> {
            getInfoFromEditTexts();
            if (!name.equals("")) {
                if(checkEditCondition(bIn)){
                    updateGymProgramInfo();
                }else {
                    createProgramAndAddExercises();
                }
            }else{
                Toast toast = Toast.makeText(this, "Insert program name!", Toast.LENGTH_SHORT);
                toast.show();
            }
        });

    }


    private boolean checkEditCondition(Bundle b){
        if (b != null)
            return b.getBoolean("edit");
        return false;
    }

    private void getInfoFromEditTexts() {
        name = gymProgramName.getText().toString();
        goal = gymProgramGoal.getText().toString();

        duration = getProperIntFromDurationEditText(gymProgramDuration);
        difficulty = getProperIntFromDifficultyEditText(gymProgramDifficulty);
        description = gymProgramDescription.getText().toString();
    }

    private void loadGymProgramInfo(){
        program = (GymProgram) getIntent().getSerializableExtra("GymProgram");
        if (program != null){
            gymProgramName.setText(program.getName());
            gymProgramDescription.setText(program.getDescription());
            gymProgramGoal.setText(program.getGoal());
            gymProgramDuration.setText(program.getDuration());
            gymProgramDifficulty.setText(String.valueOf(program.getDifficulty()));
        }else
            Log.e("GYM PROGRAM INFO", "Unable to load program for edit");

        addExerciseInstruction.setText(R.string.save_changes_to_program);
        storeGymProgramInfoBtn.setImageResource(android.R.drawable.ic_menu_save);

    }

    private void setActivityTitle(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.add_program);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void updateGymProgramInfo(){
        GymProgram programInfo = new GymProgram(name, description, goal, duration, difficulty);
        programInfo.setGymProgramID(program.getGymProgramID());
        try (DatabaseHandler dbHelper = new DatabaseHandler(this)) {
            dbHelper.updateGymProgramInfo(programInfo);
            Toast toast = Toast.makeText(this, "Gym program updated successfully!", Toast.LENGTH_SHORT);
            toast.show();
            Intent out = new Intent();
            setResult(RESULT_OK, out);
            finish();
        }catch (Exception e){
            Toast toast = Toast.makeText(this, "Unable to access database", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void createProgramAndAddExercises(){
        GymProgram programInfo = new GymProgram(name, description, goal, duration, difficulty);

        Intent i = new Intent(this, AddGymProgramExercisesActivity.class);
        i.putExtra("GymProgramInfo", programInfo);
        i.putExtra("new", true);
        startActivity(i);
    }

    private int getProperIntFromDurationEditText(EditText editText){
        int quantity = 0;
        String text = editText.getText().toString();
        if(text.equals("")){
            quantity = DEFAULT_DURATION;
        }else{
            text = text.replace(" min","");
            quantity = Integer.parseInt(text);
            if (quantity < DURATION_LOWER_BOUND)  quantity = DURATION_LOWER_BOUND;
        }
        return quantity;
    }

    private int getProperIntFromDifficultyEditText(EditText editText){
        int quantity = 0;
        String text = editText.getText().toString();
        if(text.equals("")){
            quantity = DEFAULT_DIFFICULTY;
        }else{
            quantity = Integer.parseInt(text);
            if (quantity < DIFFICULTY_LOWER_BOUND)  quantity = DIFFICULTY_LOWER_BOUND;
            if (quantity > DIFFICULTY_UPPER_BOUND) quantity = DIFFICULTY_UPPER_BOUND;
        }
        return quantity;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        int id = item.getItemId();
        if (id == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}