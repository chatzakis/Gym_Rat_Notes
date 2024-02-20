package com.example.gymbuddy;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class DatabaseHandler extends SQLiteOpenHelper {
    private final Context content;
    private static final String DATABASE_NAME = "database.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_EXERCISE = "Exercise";
    private static final String TABLE_ENTRY = "Entry";
    private static final String TABLE_GYM_PROGRAM = "GymProgram";

    // General
    private static final String COLUMN_ID = "ID";

    // Table exercise
    private static final String COLUMN_NAME = "Name";
    private static final String COLUMN_DESCRIPTION = "Description";
    private static final String COLUMN_PHOTO = "Photo";
    private static final String COLUMN_MGROUP_ID = "MGroup_ID";
    private static final String COLUMN_M2GROUP_ID = "M2Group_ID";

    // Table entry
    private static final String COLUMN_DATE = "Date" ;
    private static final String COLUMN_EXERCISE_ID= "Exercise_ID";
    private static final String COLUMN_WEIGHT= "Weight" ;
    private static final String COLUMN_SETS = "Sets" ;
    private static final String COLUMN_REPS= "Reps";
    private static final String COLUMN_DURATION= "Duration";
    private static final String COLUMN_REST_TIME= "Rest";
    private static final String COLUMN_PACE= "Pace";
    private static final String COLUMN_COMMENTS="Comments";

    //Table GymProgram
    private static final String COLUMN_DIFFICULTY = "Difficulty";
    private static final String COLUMN_GOAL = "Goal";
    private static final String COLUMN_PROGRAM_EXERCISES = "ProgramExercises";

    private static final String DATABASE_FUNCTION = "Database functions:";
    private static final String OK_MESSAGE = "DATA FUNCTION OK";
    private static final String ERROR_MESSAGE = "DATA FUNCTION ERROR";

    private DatabaseFunctions dbFunctions;


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.content = context;
        try  {
            dbFunctions = new DatabaseFunctions(content);
            Log.i(DATABASE_FUNCTION, OK_MESSAGE);
        }
        catch (Exception e){
            Log.e(DATABASE_FUNCTION, ERROR_MESSAGE);
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createExerciseTable = "CREATE TABLE " + TABLE_EXERCISE + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_NAME + " VARCHAR(30)," +
                COLUMN_DESCRIPTION + " TEXT," +
                COLUMN_PHOTO + " INTEGER," +
                COLUMN_MGROUP_ID + " INTEGER," +
                COLUMN_M2GROUP_ID + " INTEGER)";
        db.execSQL(createExerciseTable);

        String createEntryTable = "CREATE TABLE " + TABLE_ENTRY + " (" +
                COLUMN_ID + " INTEGER  NOT NULL," +
                COLUMN_DATE + " DATETIME," +
                COLUMN_EXERCISE_ID + " INTEGER," +
                COLUMN_WEIGHT + " TEXT," +
                COLUMN_SETS + " TEXT," +
                COLUMN_REPS + " TEXT," +
                COLUMN_DURATION + " TEXT," +
                COLUMN_REST_TIME + " TEXT," +
                COLUMN_PACE + " TEXT," +
                COLUMN_COMMENTS + " TEXT," +
                "PRIMARY KEY(ID)," +
                "  FOREIGN KEY(Exercise_ID)" +
                "    REFERENCES Exercise(ID)" +
                "      ON DELETE SET NULL" +
                "      ON UPDATE NO ACTION)";
        db.execSQL(createEntryTable);

        String createProgramTable = "CREATE TABLE " + TABLE_GYM_PROGRAM + " (" +
                COLUMN_ID + " INTEGER  NOT NULL," +
                COLUMN_NAME + " TEXT," +
                COLUMN_DESCRIPTION + " TEXT," +
                COLUMN_GOAL + " TEXT," +
                COLUMN_DURATION + " INTEGER," +
                COLUMN_DIFFICULTY + " INTEGER," +
                COLUMN_PROGRAM_EXERCISES + " TEXT," +
                "PRIMARY KEY(ID))";
        db.execSQL(createProgramTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXERCISE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ENTRY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GYM_PROGRAM);
        onCreate(db);
    }

    public void resetDatabase() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXERCISE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ENTRY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GYM_PROGRAM);

        onCreate(db);
    }

    public boolean checkTableEmpty(String tableName){
        return dbFunctions.checkTableEmpty(tableName);
    }

    // Exercise Table
    public void addExercise(Exercise e) {
        boolean exists = (dbFunctions.checkIdExistsInTable(e.getId(), TABLE_EXERCISE) || dbFunctions.checkNameExistsInTable(e.getName(), TABLE_EXERCISE));

        if (!exists) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_NAME, e.getName());
            values.put(COLUMN_DESCRIPTION, e.getDescription());
            values.put(COLUMN_MGROUP_ID, e.getMain_muscle_group());
            values.put(COLUMN_M2GROUP_ID, e.getSecondary_muscle_group());
            values.put(COLUMN_PHOTO, e.getPhotograph());
            dbFunctions.addRecordToDatabase(values, TABLE_EXERCISE);
        } else
            Toast.makeText(content, e.getName() + " already exists", Toast.LENGTH_SHORT).show();
    }

    public void addExerciseWithoutMessage(Exercise e) {
        boolean exists = (dbFunctions.checkIdExistsInTable(e.getId(), TABLE_EXERCISE) || dbFunctions.checkNameExistsInTable(e.getName(), TABLE_EXERCISE));

        if (!exists) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_NAME, e.getName());
            values.put(COLUMN_DESCRIPTION, e.getDescription());
            values.put(COLUMN_MGROUP_ID, e.getMain_muscle_group());
            values.put(COLUMN_M2GROUP_ID, e.getSecondary_muscle_group());
            values.put(COLUMN_PHOTO, e.getPhotograph());
            dbFunctions.addRecordToDatabase(values, TABLE_EXERCISE);
        }
    }

    public void updateExercise(Exercise e) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, e.getName());
        values.put(COLUMN_DESCRIPTION, e.getDescription());
        values.put(COLUMN_MGROUP_ID, e.getMain_muscle_group());
        values.put(COLUMN_M2GROUP_ID, e.getSecondary_muscle_group());
        values.put(COLUMN_PHOTO, e.getPhotograph());

        dbFunctions.updateRecordInDatabase(values, TABLE_EXERCISE, e.getId());
    }

    public List<Exercise> getExercises() {
        Cursor cursor = dbFunctions.getAllRecordsFromTableDescending(TABLE_EXERCISE);
        List<Exercise> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndex(COLUMN_ID));
            String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
            String description = cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION));
            int photo = cursor.getInt(cursor.getColumnIndex(COLUMN_PHOTO));
            long mainGroup = cursor.getLong(cursor.getColumnIndex(COLUMN_MGROUP_ID));
            long secGroup = cursor.getLong(cursor.getColumnIndex(COLUMN_M2GROUP_ID));
            list.add(new Exercise(id, name, mainGroup*10+secGroup, description, photo));
        }
        cursor.close();
        return list;
    }

    public Exercise getExerciseByID(long exerciseID) {
        Cursor cursor = dbFunctions.getRecordsById(TABLE_EXERCISE, exerciseID);
        Exercise exercise = new Exercise();
        while (cursor.moveToNext()) {
            exercise.setId(cursor.getLong(cursor.getColumnIndex(COLUMN_ID)));
            exercise.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
            exercise.setDescription(cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION)));
            exercise.setPhotograph(cursor.getInt(cursor.getColumnIndex(COLUMN_PHOTO)));
            long mainGroup = cursor.getLong(cursor.getColumnIndex(COLUMN_MGROUP_ID));
            long secGroup = cursor.getLong(cursor.getColumnIndex(COLUMN_M2GROUP_ID));
            exercise.setMuscle_group((int) (mainGroup*10+secGroup));
        }
        cursor.close();
        return exercise;
    }

    public List<Exercise> getExerciseByMainGroup(long mainGroupID) {
        String query = "SELECT * FROM " + TABLE_EXERCISE+
                " WHERE "+ COLUMN_MGROUP_ID + " = "+ mainGroupID;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        List<Exercise> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndex(COLUMN_ID));
            String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
            String description = cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION));
            int photo = cursor.getInt(cursor.getColumnIndex(COLUMN_PHOTO));
            long mainGroup = cursor.getLong(cursor.getColumnIndex(COLUMN_MGROUP_ID));
            long secGroup = cursor.getLong(cursor.getColumnIndex(COLUMN_M2GROUP_ID));
            list.add(new Exercise(id, name, mainGroup*10+secGroup, description, photo));
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<Exercise> getExerciseByGroup(long mainGroupID, int secGroupID) {
        String query = "SELECT * FROM " + TABLE_EXERCISE+
        " WHERE "+ COLUMN_MGROUP_ID + " = "+ mainGroupID + " AND " + COLUMN_M2GROUP_ID + " = "+ secGroupID;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        List<Exercise> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndex(COLUMN_ID));
            String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
            String description = cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION));
            int photo = cursor.getInt(cursor.getColumnIndex(COLUMN_PHOTO));
            long mainGroup = cursor.getLong(cursor.getColumnIndex(COLUMN_MGROUP_ID));
            long secGroup = cursor.getLong(cursor.getColumnIndex(COLUMN_M2GROUP_ID));
            list.add(new Exercise(id, name, mainGroup*10+secGroup, description, photo));
        }
        cursor.close();
        db.close();
        return list;
    }

    public void deleteExercise(long exerciseId) {
        dbFunctions.deleteRecord(TABLE_EXERCISE, exerciseId);
    }

    // Table entry
    public void addEntry(Entry o) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_WEIGHT, o.getWeight());
        values.put(COLUMN_SETS, o.getSets());
        values.put(COLUMN_REPS, o.getReps());
        values.put(COLUMN_COMMENTS, o.getComments());
        values.put(COLUMN_EXERCISE_ID, o.getExerciseId());
        values.put(COLUMN_DURATION, o.getDuration());
        values.put(COLUMN_REST_TIME, o.getRest_time());
        values.put(COLUMN_PACE,o.getPace());
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        values.put(COLUMN_DATE, formatter.format(date));

        dbFunctions.addRecordToDatabase(values, TABLE_ENTRY);
    }

    public void updateEntry(Entry o) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_WEIGHT, o.getWeight());
        values.put(COLUMN_SETS, o.getSets());
        values.put(COLUMN_REPS, o.getReps());
        values.put(COLUMN_COMMENTS, o.getComments());
        values.put(COLUMN_DURATION, o.getDuration());
        values.put(COLUMN_REST_TIME, o.getRest_time());
        values.put(COLUMN_PACE,o.getPace());

        dbFunctions.updateRecordInDatabase(values, TABLE_ENTRY, o.getEntry_id());
    }

    public List<Entry> getEntries(int limit) {
        Cursor cursor = dbFunctions.getAllRecordsFromTableDescending(TABLE_ENTRY);
        List<Entry> list = new ArrayList<>();
        while (cursor.moveToNext() && limit>0) {
            long id = cursor.getLong(cursor.getColumnIndex(COLUMN_ID));
            String date = formatDate(cursor.getString(cursor.getColumnIndex(COLUMN_DATE)));
            long exerciseId =  cursor.getLong(cursor.getColumnIndex(COLUMN_EXERCISE_ID));
            String weight =  cursor.getString(cursor.getColumnIndex(COLUMN_WEIGHT));
            String reps = cursor.getString(cursor.getColumnIndex(COLUMN_REPS));
            String sets = cursor.getString(cursor.getColumnIndex(COLUMN_SETS));
            String comments = cursor.getString(cursor.getColumnIndex(COLUMN_COMMENTS));
            String duration = cursor.getString(cursor.getColumnIndex(COLUMN_DURATION));
            String rest = cursor.getString(cursor.getColumnIndex(COLUMN_REST_TIME));
            String pace = cursor.getString(cursor.getColumnIndex(COLUMN_PACE));
            list.add(new Entry(id, date, exerciseId, weight, sets, reps, duration,rest,pace, comments, dbFunctions.getNameByID(TABLE_EXERCISE, exerciseId)));
            limit --;
        }
        cursor.close();
        return list;
    }

    public List<Entry> getRelevantEntries(long exerciseId) {
        String query = "SELECT * FROM " + TABLE_ENTRY +
                " WHERE "+ COLUMN_EXERCISE_ID + " = "+ exerciseId + " ORDER BY " + COLUMN_ID + " DESC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        List<Entry> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndex(COLUMN_ID));
            String date = formatDate(cursor.getString(cursor.getColumnIndex(COLUMN_DATE)));
            String weight =  cursor.getString(cursor.getColumnIndex(COLUMN_WEIGHT));
            String reps = cursor.getString(cursor.getColumnIndex(COLUMN_REPS));
            String sets = cursor.getString(cursor.getColumnIndex(COLUMN_SETS));
            String comments = cursor.getString(cursor.getColumnIndex(COLUMN_COMMENTS));
            String duration = cursor.getString(cursor.getColumnIndex(COLUMN_DURATION));
            String rest = cursor.getString(cursor.getColumnIndex(COLUMN_REST_TIME));
            String pace = cursor.getString(cursor.getColumnIndex(COLUMN_PACE));
            list.add(new Entry(id, date, exerciseId, weight, sets, reps, duration,rest,pace, comments, dbFunctions.getNameByID(TABLE_EXERCISE, exerciseId)));
        }
        cursor.close();
        db.close();
        return list;
    }

    public String formatDate(String date){
        return date.substring(8,10)+'/'+ date.substring(5,7)+'/' +date.substring(0,4);
    }

    public void deleteEntry(long entryId) {
        dbFunctions.deleteRecord(TABLE_ENTRY, entryId);
    }


    // Gym Programs Table
    public void addGymProgram(GymProgram program) {

        boolean exists = (dbFunctions.checkIdExistsInTable(program.getGymProgramID(), TABLE_GYM_PROGRAM) ||
                dbFunctions.checkNameExistsInTable(program.getName(), TABLE_GYM_PROGRAM));

        if (!exists) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_NAME, program.getName());
            values.put(COLUMN_DESCRIPTION, program.getDescription());
            values.put(COLUMN_GOAL, program.getGoal());
            values.put(COLUMN_DIFFICULTY, program.getDifficulty());
            values.put(COLUMN_DURATION, program.getDuration());
            values.put(COLUMN_PROGRAM_EXERCISES, program.storeExercisesToString());

            dbFunctions.addRecordToDatabase(values,TABLE_GYM_PROGRAM);
        }
        else
            Toast.makeText(content, program.getName() + " already exists",Toast.LENGTH_SHORT).show();
    }

    public List<GymProgram> getGymPrograms() {
        Cursor cursor = dbFunctions.getAllRecordsFromTableDescending(TABLE_GYM_PROGRAM);
        List<GymProgram> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndex(COLUMN_ID));
            String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
            String description = cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION));
            String goal = cursor.getString(cursor.getColumnIndex(COLUMN_GOAL));
            int duration = cursor.getInt(cursor.getColumnIndex(COLUMN_DURATION));
            int difficulty = cursor.getInt(cursor.getColumnIndex(COLUMN_DIFFICULTY));
            String exercises = cursor.getString(cursor.getColumnIndex(COLUMN_PROGRAM_EXERCISES));
            list.add(new GymProgram((int) id, name, exercises, description, goal, duration,difficulty));
        }
        cursor.close();
        return list;
    }

    public GymProgram getGymProgramByID(long gymProgramID) {
        Cursor cursor = dbFunctions.getRecordsById(TABLE_GYM_PROGRAM, gymProgramID);
        GymProgram program;
        while (cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndex(COLUMN_ID));
            String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
            String description = cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION));
            String goal = cursor.getString(cursor.getColumnIndex(COLUMN_GOAL));
            int duration = cursor.getInt(cursor.getColumnIndex(COLUMN_DURATION));
            int difficulty = cursor.getInt(cursor.getColumnIndex(COLUMN_DIFFICULTY));
            String exercises = cursor.getString(cursor.getColumnIndex(COLUMN_PROGRAM_EXERCISES));
            program = new GymProgram((int) id, name, exercises, description, goal, duration,difficulty);
            return program;
        }
        cursor.close();
        return new GymProgram();
    }

    public void updateGymProgram(GymProgram program) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, program.getName());
        values.put(COLUMN_DESCRIPTION, program.getDescription());
        values.put(COLUMN_GOAL, program.getGoal());
        values.put(COLUMN_DIFFICULTY, program.getDifficulty());
        values.put(COLUMN_DURATION,program.getDuration());
        values.put(COLUMN_PROGRAM_EXERCISES, program.storeExercisesToString());

        dbFunctions.updateRecordInDatabase(values, TABLE_GYM_PROGRAM, program.getGymProgramID());
    }

    public void updateGymProgramInfo(GymProgram program) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, program.getName());
        values.put(COLUMN_DESCRIPTION, program.getDescription());
        values.put(COLUMN_GOAL, program.getGoal());
        values.put(COLUMN_DIFFICULTY, program.getDifficulty());
        values.put(COLUMN_DURATION,program.getDuration());

        dbFunctions.updateRecordInDatabase(values, TABLE_GYM_PROGRAM, program.getGymProgramID());

    }

    public void deleteGymProgram(long gymProgramId) {
        dbFunctions.deleteRecord(TABLE_GYM_PROGRAM, gymProgramId);
    }


}
