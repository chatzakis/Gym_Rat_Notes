package com.example.gymbuddy;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ProgramExercisesRecyclerAdapter extends RecyclerView.Adapter<ProgramExercisesRecyclerAdapter.ViewHolder>{
    private final Activity activity;
    private ArrayList<Exercise> exercises;

    private ArrayList<Integer> selectedExercisesIds;

    public ProgramExercisesRecyclerAdapter(Activity activity) {
        this.activity = activity;
        this.exercises = new ArrayList<>();
        this.selectedExercisesIds = new ArrayList<>();
    }

    public void setSelectedExercisesIds( ArrayList<Integer> selectedExercises){
        this.selectedExercisesIds = selectedExercises;
    }

    public ArrayList<Integer> getSelectedExercisesIds(){
        return selectedExercisesIds;
    }

    public boolean hasSelections(){
        return !selectedExercisesIds.isEmpty();
    }

    @NonNull
    @Override
    public com.example.gymbuddy.ProgramExercisesRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.program_ex_layout, parent, false);
        return new com.example.gymbuddy.ProgramExercisesRecyclerAdapter.ViewHolder(v);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull com.example.gymbuddy.ProgramExercisesRecyclerAdapter.ViewHolder holder, int position) {
        Exercise exercise = exercises.get(position);
        holder.nameView.setText(exercise.getName());
        holder.groupView.setText(exercise.getGroup_name());
        //Card Color and images
        holder.card.setBackgroundColor(exercise.getGroupColor());
        holder.muscleImage.setImageResource(exercise.getGroupImage());
        markSelectedExercises(holder, position);
        holder.setItemClickListener((view, pos) -> selectDeselectExercise(holder, position));
    }

    private void selectDeselectExercise(ViewHolder holder, int pos){
        Exercise exercise = exercises.get(pos);
        int exerciseID = (int)exercises.get(pos).getId();
        if (selectedExercisesIds.contains(exerciseID)) {
            selectedExercisesIds.remove(Integer.valueOf(exerciseID));
            notifyDataSetChanged();
        }
        else
            selectedExercisesIds.add((int)exercise.getId());
        markSelectedExercises(holder, pos);
    }

    @SuppressLint("ResourceAsColor")
    private void markSelectedExercises(ViewHolder holder, int pos){
        int exerciseID = (int)exercises.get(pos).getId();
        if (selectedExercisesIds.contains(exerciseID)){
            holder.selectedExerciseNumber.setBackgroundColor(R.color.mp_blue);
            int exerciseOrder = selectedExercisesIds.indexOf(exerciseID) + 1;
            holder.selectedExerciseNumber.setText(String.valueOf(exerciseOrder));
            holder.selectedExerciseNumber.setVisibility(View.VISIBLE);
        }else
            holder.selectedExerciseNumber.setVisibility(View.GONE);
    }



    public void setExercises(List<Exercise> exercises) {
        this.exercises = new ArrayList<>(exercises);
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameView;
        private final TextView groupView;
        private final TextView selectedExerciseNumber;
        private final CardView card;
        public ImageView muscleImage;
        private ItemClickListener itemClickListener;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameView = itemView.findViewById(R.id.ex_name);
            groupView = itemView.findViewById(R.id.m_group);
            muscleImage = itemView.findViewById(R.id.muscleGroupPhoto);
            card = itemView.findViewById(R.id.card_view);
            selectedExerciseNumber = itemView.findViewById(R.id.selectedExerciseOrder);
            itemView.setOnClickListener(v -> itemClickListener.onClick(v, getAdapterPosition()));
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

    }
}

