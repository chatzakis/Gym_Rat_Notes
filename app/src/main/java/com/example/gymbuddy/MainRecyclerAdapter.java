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
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainRecyclerAdapter extends RecyclerView.Adapter<MainRecyclerAdapter.ViewHolder> {
    private final Activity activity;
    private List<Exercise> exercises;



    public MainRecyclerAdapter(Activity activity) {
        this.activity = activity;
        this.exercises = new ArrayList<>();

    }

    public void goToExercises( int position) {
        Exercise e = exercises.get(position);
        Intent i = new Intent(activity, EntriesActivity.class);
        i.putExtra(AddExercise.EXERCISE_ID, e.getId());
        i.putExtra(AddExercise.EXERCISE_NAME, e.getName());
        i.putExtra(MainActivity.MUSCLE_GROUP, e.getMain_muscle_group());
        activity.startActivity(i);
    }

    @NonNull
    @Override
    public MainRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.simple_ex_layout, parent, false);
        return new MainRecyclerAdapter.ViewHolder(v);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull MainRecyclerAdapter.ViewHolder holder, int position) {
        Exercise exercise = exercises.get(position);
        holder.nameView.setText(exercise.getName());
        holder.groupView.setText(exercise.getGroup_name());
        //Card Color and images
        holder.card.setBackgroundColor(exercise.getGroupColor());
        holder.muscleImage.setImageResource(exercise.getGroupImage());
        holder.setItemClickListener((view, position1) -> goToExercises(position1));

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
        private final CardView card;
        public ImageView muscleImage;
        private ItemClickListener itemClickListener;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameView = itemView.findViewById(R.id.ex_name);
            groupView = itemView.findViewById(R.id.m_group);
            muscleImage = itemView.findViewById(R.id.muscleGroupPhoto);
            card = itemView.findViewById(R.id.card_view);
            itemView.setOnClickListener(v -> itemClickListener.onClick(v, getAdapterPosition()));
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }


    }
}
