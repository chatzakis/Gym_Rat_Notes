package com.example.gymbuddy;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class GymProgramsRecyclingAdapter extends RecyclerView.Adapter<GymProgramsRecyclingAdapter.ViewHolder>{
    private final Activity activity;
    private List<GymProgram> gymPrograms;
    public GymProgramsRecyclingAdapter(Activity activity) {
        this.activity = activity;
        this.gymPrograms = new ArrayList<>();

    }

    public void goToGymProgram(int position) {
        GymProgram program = gymPrograms.get(position);
        Intent i = new Intent(activity, SingleGymProgramActivity.class);
        i.putExtra(SingleGymProgramActivity.GYM_PROGRAM_ID, program.getGymProgramID());
        i.putExtra(SingleGymProgramActivity.GYM_PROGRAM_NAME, program.getName());
        activity.startActivity(i);
    }

    @NonNull
    @Override
    public GymProgramsRecyclingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.gym_program_card, parent, false);
        return new GymProgramsRecyclingAdapter.ViewHolder(v);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull GymProgramsRecyclingAdapter.ViewHolder holder, int position) {
        GymProgram program = gymPrograms.get(position);
        holder.nameTextView.setText(program.getName());
        holder.goalTextView.setText(program.getGoal());
        holder.durationTextView.setText(program.getDuration());
        holder.exerciseCountTextView.setText(String.valueOf(program.getExerciseCount()));
        holder.setItemClickListener((view, position1) -> goToGymProgram(position1));
        for (int i = 0; i< program.getDifficulty(); i++){
            holder.difficultyFireImages[i].setImageResource(R.drawable.redfire);
        }

    }

    public void setGymPrograms(List<GymProgram> programs) {
        this.gymPrograms = new ArrayList<>(programs);
    }

    @Override
    public int getItemCount() {
        return gymPrograms.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameTextView;
        private final TextView goalTextView;
        private final TextView exerciseCountTextView;
        private final TextView durationTextView;
        private final ImageView[] difficultyFireImages = new ImageView[3];
        private ItemClickListener itemClickListener;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.programName);
            goalTextView = itemView.findViewById(R.id.programGoal);
            exerciseCountTextView = itemView.findViewById(R.id.exerciseCounter);
            durationTextView = itemView.findViewById(R.id.programDuration);
            difficultyFireImages[0] = itemView.findViewById(R.id.flame1);
            difficultyFireImages[1] = itemView.findViewById(R.id.flame2);
            difficultyFireImages[2] = itemView.findViewById(R.id.flame3);
            itemView.setOnClickListener(v -> itemClickListener.onClick(v, getAdapterPosition()));
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }


    }
}

