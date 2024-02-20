package com.example.gymbuddy;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class EntriesRecyclerAdapter extends RecyclerView.Adapter<EntriesRecyclerAdapter.ViewHolder> {
    private final Activity activity;
    private List<Entry> entries;

    boolean nameMode;

    public EntriesRecyclerAdapter(Activity activity) {
        this.activity = activity;
        this.entries = new ArrayList<>();
        this.nameMode = false;
    }
    public EntriesRecyclerAdapter(Activity activity, boolean nameMode) {
        this.activity = activity;
        this.entries = new ArrayList<>();
        this.nameMode = nameMode;
    }

    @NonNull
    @Override
    public EntriesRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.entry_card_layout, parent, false);
        return new EntriesRecyclerAdapter.ViewHolder(v);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    // Add entries
    public void onBindViewHolder(@NonNull EntriesRecyclerAdapter.ViewHolder holder, int position) {
        Entry entry = entries.get(position);
        initialiseHolder(holder, entry);
        displayOnlyNecessaryFields(holder, entry);

        holder.setItemClickListener((view, position1) -> showDialog(position1, view));
    }

    public void initialiseHolder(ViewHolder holder, Entry entry){
        holder.date.setText(entry.getDate());
        holder.weight.setText(entry.getWeight());
        holder.reps.setText(entry.getReps());
        holder.sets.setText(entry.getSets());
        holder.duration.setText(entry.getDuration());
        holder.pace.setText(entry.getPace());
        holder.rest.setText(entry.getRest_time());
        holder.comments.setText(entry.getComments());
        holder.name.setText(entry.getExerciseName());
    }

    private void displayOnlyNecessaryFields(ViewHolder holder, Entry entry) {
        if(!nameMode){
            holder.name.setVisibility(View.GONE);
        }else{holder.name.setVisibility(View.VISIBLE);}
        // Content
        if(entry.getWeight().equals("")){
            holder.weightRow.setVisibility(View.GONE);
        }else{holder.weightRow.setVisibility(View.VISIBLE);}
        if(entry.getReps().equals("")){
            holder.repsRow.setVisibility(View.GONE);
        }else{holder.repsRow.setVisibility(View.VISIBLE);}
        if(entry.getSets().equals("")){
            holder.setsRow.setVisibility(View.GONE);
        }else{holder.setsRow.setVisibility(View.VISIBLE);}
        if(entry.getDuration().equals("")){
            holder.durationRow.setVisibility(View.GONE);
        }else{holder.durationRow.setVisibility(View.VISIBLE);}
        if(entry.getPace().equals("")){
            holder.paceRow.setVisibility(View.GONE);
        }else{holder.paceRow.setVisibility(View.VISIBLE);}
        if(entry.getRest_time().equals("")){
            holder.restRow.setVisibility(View.GONE);
        }else{holder.restRow.setVisibility(View.VISIBLE);}
        if(entry.getComments().equals("")){
            holder.commentsRow.setVisibility(View.GONE);
        }else{holder.commentsRow.setVisibility(View.VISIBLE);}
    }

    private void showDialog(int position, View v) {
        String[] choices = {activity.getString(R.string.edit), activity.getString(R.string.delete)};
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(entries.get(position).getExerciseName() + " - " + entries.get(position).getDate());
        builder.setCancelable(true);
        builder.setNegativeButton("Cancel", null);
        builder.setItems(choices, (dialog, which) -> {
            Entry e = entries.get(position);
            if (which == 0) {// edit
                Intent i = new Intent(activity, AddEntryActivity.class);
                i.putExtra(AddEntryActivity.ENTRY_ID, e.getEntry_id());
                i.putExtra(AddEntryActivity.EXERCISE_ID, e.getExerciseId());
                i.putExtra(AddEntryActivity.ENTRY_DATE, e.getDate());
                i.putExtra(AddEntryActivity.ENTRY_WEIGHT, e.getWeight());
                i.putExtra(AddEntryActivity.ENTRY_REPS, e.getReps());
                i.putExtra(AddEntryActivity.ENTRY_SETS, e.getSets());
                i.putExtra(AddEntryActivity.ENTRY_DURATION, e.getDuration());
                i.putExtra(AddEntryActivity.ENTRY_REST, e.getRest_time());
                i.putExtra(AddEntryActivity.ENTRY_PACE, e.getPace());
                i.putExtra(AddEntryActivity.ENTRY_COMMENTS, e.getComments());
                activity.startActivityForResult(i,1);
            }else if(which==1) { // delete
                try (DatabaseHandler dbHelper = new DatabaseHandler(activity)) {
                    dbHelper.deleteEntry(e.getEntry_id());
                    entries.remove(position);
                    notifyItemRemoved(position);
                    Snackbar.make(v, e.getDate() + " entry deleted ", Snackbar.LENGTH_LONG).show();
                }catch (Exception expt){
                    Snackbar.make(v, "Unable to delete entry " + position, Snackbar.LENGTH_LONG).show();
                }

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void setEntries(List<Entry> entries) {
        this.entries = new ArrayList<>(entries);
    }

    @Override
    public int getItemCount() {
        return entries.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView name;
        private final TextView date;
        private final TextView weight;
        private final TextView reps;
        private final TextView sets;
        private final TextView duration;
        private final TextView rest;
        private final TextView pace;
        private final TextView comments;
        private final TableRow weightRow;
        private final TableRow repsRow;
        private final TableRow setsRow;
        private final TableRow durationRow;
        private final TableRow restRow;
        private final TableRow paceRow;
        private final TableRow commentsRow;
        private ItemClickListener itemClickListener;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.exerciseName);
            date = itemView.findViewById(R.id.date);
            weight = itemView.findViewById(R.id.weight);
            reps = itemView.findViewById(R.id.reps);
            sets = itemView.findViewById(R.id.sets);
            duration = itemView.findViewById(R.id.duration);
            rest = itemView.findViewById(R.id.rest);
            pace = itemView.findViewById(R.id.pace);
            comments = itemView.findViewById(R.id.comments);
            weightRow = itemView.findViewById(R.id.rowWeight);
            repsRow= itemView.findViewById(R.id.rowReps);
            setsRow = itemView.findViewById(R.id.rowSets);
            durationRow = itemView.findViewById(R.id.rowDuration);
            restRow = itemView.findViewById(R.id.rowRest);
            paceRow = itemView.findViewById(R.id.rowPace);
            commentsRow = itemView.findViewById(R.id.rowComments);
            itemView.setOnClickListener(v -> itemClickListener.onClick(v, getAdapterPosition()));
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }


    }
}

