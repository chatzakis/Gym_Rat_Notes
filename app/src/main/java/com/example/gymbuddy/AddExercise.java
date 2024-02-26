package com.example.gymbuddy;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

public class AddExercise extends AppCompatActivity {
    public static final String EXERCISE_ID = "id";
    public static final String EXERCISE_NAME = "name";

    public int mainMuscleGroup;
    public int secMuscleGroup;

    public boolean pause;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exercise);

        //Components
        EditText ex_name = findViewById(R.id.exercise_name);
        Spinner mainSpinner = findViewById(R.id.select_group);
        Spinner secondarySpinner = findViewById(R.id.select_secontary_group);
        ImageView demoMuscleImg = findViewById(R.id.demoMuscleView);
        EditText exerciseDescription = findViewById(R.id.exerciseDescriptionInput);

        FloatingActionButton update_exercise = findViewById(R.id.addExercise);
        FloatingActionButton homeBtn = findViewById(R.id.home_button);

        Bundle b = getIntent().getExtras();
        mainMuscleGroup = 0;
        if (b != null)
            if (b.getBoolean("new")){
                mainMuscleGroup = b.getInt("muscleGroup");

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                    this,
                    changeSecondarySpinner(mainMuscleGroup),
                    android.R.layout.simple_spinner_item
            );

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            secondarySpinner.setAdapter(adapter);

            mainSpinner.setSelection(mainMuscleGroup);
        }

        try (DatabaseHandler dbHelper = new DatabaseHandler(this)) {
            long exerciseID = -1;
            if (b != null)
                if (b.getBoolean("edit"))
                    exerciseID = b.getLong("exerciseID");

            int actionBarTitle = R.string.new_exercise;
            

            if (exerciseID == -1) {
                update_exercise.setImageResource(android.R.drawable.ic_menu_save);
            } else {
                actionBarTitle = R.string.edit_exercise;
                update_exercise.setImageResource(android.R.drawable.stat_notify_sync);
                Exercise exercise = dbHelper.getExerciseByID(exerciseID);
                ex_name.setText(exercise.getName());
                exerciseDescription.setText(exercise.getDescription());

                mainMuscleGroup = (int) exercise.getMain_muscle_group();
                secMuscleGroup = (int) exercise.getSecondary_muscle_group();
                pause = true;

                if (mainMuscleGroup == -1) { // no category
                    mainSpinner.setSelection(0);
                } else {
                    mainSpinner.setSelection(mainMuscleGroup);
                }

            }

            setActivityTitle(actionBarTitle);

            final long exID = exerciseID;
            update_exercise.setOnClickListener(view -> {
                String name = ex_name.getText().toString();
                String description = exerciseDescription.getText().toString();
                int group = mainSpinner.getSelectedItemPosition();
                int secondaryGroup = secondarySpinner.getSelectedItemPosition();
                if (!name.equals("")) {
                    Exercise e = new Exercise(exID, name,
                            getGroupsFromSpinners(group, secondaryGroup), description);
                    if (exID == -1) {
                        String result = dbHelper.addExercise(e);
                        Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
                    } else {
                        dbHelper.updateExercise(e);
                    }
                    goToExercisesActivity(group);
                }
            });
        }catch (Exception e){
            Toast toast = Toast.makeText(this, "Unable to access database", Toast.LENGTH_SHORT);
            toast.show();
        }

        mainSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(
                            getApplicationContext(),
                            changeSecondarySpinner(position),
                            android.R.layout.simple_spinner_item
                    );

                    adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    secondarySpinner.setAdapter(adapter2);
                    Exercise sample_ex = new Exercise(0, "Sample", id * 10);
                    demoMuscleImg.setImageResource(sample_ex.getGroupImage());
                    if (secMuscleGroup != 0)
                        secondarySpinner.setSelection(secMuscleGroup);
                    mainMuscleGroup = position;
                    if (!pause){
                        secMuscleGroup = 0;
                        secondarySpinner.setSelection(secMuscleGroup);
                    }
                    pause = false;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });


        secondarySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                secMuscleGroup = position;
                Exercise sample_ex = new Exercise(0, "Sample", getGroupsFromSpinners(mainMuscleGroup, secMuscleGroup));
                demoMuscleImg.setImageResource(sample_ex.getGroupImage());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        homeBtn.setOnClickListener(view -> {startHomeActivity();});
    }

    private void goToExercisesActivity(int group) {
        Intent out = new Intent(this, ExercisesActivity.class);
        out.putExtra(MainActivity.MUSCLE_GROUP, group);
        Toast toast = Toast.makeText(this, "Exercise Added/Updated", Toast.LENGTH_SHORT);
        toast.show();
        startActivity(out);
    }

    public void setActivityTitle(int actionBarTitle){
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
    public boolean onOptionsItemSelected (MenuItem item){
        int id = item.getItemId();
        if (id == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
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

    /* Give a two digit integer as a muscle group id the first digit represents the main muscle group
    selected by the mainSpinner and the Second digit represents the secondary muscle group
    selected by the secondarySpinner
     */
    protected int getGroupsFromSpinners(Integer mainSpinner, Integer secondarySpinner){
        return Integer.parseInt(mainSpinner.toString()+secondarySpinner.toString());
    }

    public void startHomeActivity(){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

}
