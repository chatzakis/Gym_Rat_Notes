package com.example.gymbuddy;

import android.graphics.Color;

public class Exercise {
    private long id;
    private String name;
    private long muscle_group;

    private String description;

    private int photograph;

    public Exercise(){this(-1,"",0);}
    public Exercise(long id, String name, long mg) {
        this.id = id;
        this.name = name;
        muscle_group=mg;
    }

    public Exercise(long id, String name, long mg, String description) {
        this.id = id;
        this.name = name;
        muscle_group=mg;
        this.description = description;
    }

    public Exercise(long id, String name, long mg, String description, int photograph) {
        this.id = id;
        this.name = name;
        muscle_group=mg;
        this.description = description;
        this.photograph = photograph;
    }

    public void setId(long id) { this.id = id; }
    public long getId() { return id; }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public int getPhotograph() {
        return photograph;
    }

    public void setPhotograph(int photograph) {
        this.photograph = photograph;
    }

    public long getMuscle_group() {
        return muscle_group;
    }
    public void setMuscleGroup(long muscle_group) {
        this.muscle_group = muscle_group;
    }

    public long getMain_muscle_group() {
        return (long) Math.floor(muscle_group/10);
    }

    public long getSecondary_muscle_group() {
        return muscle_group % 10;
    }

    public String getGroup_name(){
        String group;
        switch ((int)muscle_group){
            case 10:
                group = "CHEST";
                break;
            case 11:
                group = "UPPER CHEST";
                break;
            case 12:
                group = "MIDDLE CHEST";
                break;
            case 13:
                group = "LOWER CHEST";
                break;
            case 20:
                group = "BACK";
                break;
            case 21:
                group = "UPPER BACK";
                break;
            case 22:
                group = "MIDDLE BACK";
                break;
            case 23:
                group = "LOWER BACK";
                break;
            case 30:
                group = "SHOULDERS";
                break;
            case 31:
                group = "FRONT DELTS";
                break;
            case 32:
                group = "MIDDLE DELTS";
                break;
            case 33:
                group = "REAR DELTS";
                break;
            case 40:
                group = "ARMS";
                break;
            case 41:
                group = "BICEPS";
                break;
            case 42:
                group = "TRICEPS";
                break;
            case 43:
                group = "FOREARMS";
                break;
            case 50:
                group = "ABS";
                break;
            case 51:
                group = "UPPER ABS";
                break;
            case 52:
                group = "LOWER ABS";
                break;
            case 53:
                group = "SIDE ABS";
                break;
            case 60:
                group = "LEGS";
                break;
            case 61:
                group = "GLUTES";
                break;
            case 62:
                group = "QUADS";
                break;
            case 63:
                group = "HAMSTRINGS";
                break;
            case 64:
                group = "CALVES";
                break;
            case 70:
                group = "AEROBIC";
                break;
            case 71:
                group = "RUNNING";
                break;
            case 72:
                group = "BICYCLE";
                break;
            case 73:
                group = "JUMPING ROPE";
                break;
            case 74:
                group = "SWIMMING";
                break;
            default:
                group = "UNKNOWN";
        }
        return group;
    }

    public int getGroupImage(){
        int img;
        switch ((int)muscle_group) {
            case 10:
                img = R.drawable.chest;
                break;
            case 11:
                img = R.drawable.upper_chest;
                break;
            case 12:
                img = R.drawable.middle_chest;
                break;
            case 13:
                img = R.drawable.lower_chest;
                break;
            case 20:
                img = R.drawable.back;
                break;
            case 21:
                img = R.drawable.upper_back;
                break;
            case 22:
                img = R.drawable.middle_back;
                break;
            case 23:
                img = R.drawable.lower_back;
                break;
            case 30:
                img = R.drawable.shoulders;
                break;
            case 31:
                img = R.drawable.front_delt;
                break;
            case 32:
                img = R.drawable.middle_delt;
                break;
            case 33:
                img = R.drawable.rear_delt;
                break;
            case 40:
                img = R.drawable.arms;
                break;
            case 41:
                img = R.drawable.biceps;
                break;
            case 42:
                img = R.drawable.triceps;
                break;
            case 43:
                img = R.drawable.forearms;
                break;
            case 50:
                img = R.drawable.abs;
                break;
            case 51:
                img = R.drawable.upperabs;
                break;
            case 52:
                img = R.drawable.lowerabs;
                break;
            case 53:
                img = R.drawable.sideabs;
                break;
            case 60:
                img = R.drawable.legs;
                break;
            case 61:
                img = R.drawable.glutes;
                break;
            case 62:
                img = R.drawable.quads;
                break;
            case 63:
                img = R.drawable.hamstrings;
                break;
            case 64:
                img = R.drawable.calves;
                break;
            case 70:
                img = R.drawable.running;
                break;
            case 71:
                img = R.drawable.running;
                break;
            case 72:
                img = R.drawable.cycling;
                break;
            case 73:
                img = R.drawable.jumping_rope;
                break;
            case 74:
                img = R.drawable.swimming;
                break;
            default:
                img = R.drawable.running;
        }
        return img;
    }

    public int getGroupColor(){
        int color = Color.parseColor("#FF0000");
        switch ((int) muscle_group){
            case(10):
                color = Color.parseColor("#EF3F00");
                break;
            case(11):
                color = Color.parseColor("#A32C00");
                break;
            case(12):
                color = Color.parseColor("#CC3600");
                break;
            case(13):
                color = Color.parseColor("#7A2100");
                break;
            case(20):
                color = Color.parseColor("#01F025");
                break;
            case(21):
                color = Color.parseColor("#00A318");
                break;
            case(22):
                color = Color.parseColor("#00CC1F");
                break;
            case(23):
                color = Color.parseColor("#007A12");
                break;
            case(30):
                color = Color.parseColor("#C84EE1");
                break;
            case(31):
                color = Color.parseColor("#694FE0");
                break;
            case(32):
                color = Color.parseColor("#E04FB5");
                break;
            case(33):
                color = Color.parseColor("#984FE0");
                break;
            case(40):
                color = Color.parseColor("#E1B61D");
                break;
            case(41):
                color = Color.parseColor("#E0CE1D");
                break;
            case(42):
                color = Color.parseColor("#E0CE1D");
                break;
            case(43):
                color = Color.parseColor("#E0831D");
                break;
            case(50):
                color = Color.parseColor("#BBBCDE");
                break;
            case(51):
                color = Color.parseColor("#C5BADE");
                break;
            case(52):
                color = Color.parseColor("#BAC7DE");
                break;
            case(53):
                color = Color.parseColor("#D0BADE");
                break;
            case(60):
                color = Color.parseColor("#456FE1");
                break;
            case(61):
                color = Color.parseColor("#4D46E0");
                break;
            case(62):
                color = Color.parseColor("#4F1DE0");
                break;
            case(63):
                color = Color.parseColor("#46A0E0");
                break;
            case(64):
                color = Color.parseColor("#46D1E0");
                break;
            case(70):
                color = Color.parseColor("#E020AB");
                break;
            case(71):
                color = Color.parseColor("#E020AB");
                break;
            case(72):
                color = Color.parseColor("#E020AB");
                break;
            case(73):
                color = Color.parseColor("#E020AB");
                break;
            case(74):
                color = Color.parseColor("#E020AB");
                break;
            default:

        }
        return color;
    }



    public void setMuscle_group(int muscle_group) {
        this.muscle_group = muscle_group;
    }

}
