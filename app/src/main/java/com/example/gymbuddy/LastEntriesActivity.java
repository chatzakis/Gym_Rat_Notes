package com.example.gymbuddy;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.view.MenuItem;

import androidx.appcompat.widget.Toolbar;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class LastEntriesActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    EntriesRecyclerAdapter adapter = new EntriesRecyclerAdapter(this, true);
    ActionBar actionBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_last_entries);

        setActivityTitle();

        recyclerView = findViewById(R.id.entry_recycler);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        readEntries();

        FloatingActionButton homeBtn = findViewById(R.id.home_button);
        homeBtn.setOnClickListener(view -> {startHomeActivity();});

    }

    public void setActivityTitle(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.last_entries);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private boolean readEntries() {
        List<Entry> e;
        try (DatabaseHandler dbHelper = new DatabaseHandler(this)) {
            e = dbHelper.getEntries(50);
        }

        EntriesRecyclerAdapter adapter = (EntriesRecyclerAdapter) recyclerView.getAdapter();
        if (adapter != null) {
            adapter.setEntries(e);
            adapter.notifyDataSetChanged();
        } else {
            System.err.println("Adapter is null");
        }
        return !e.isEmpty();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            readEntries();
        }
    }


    public void startHomeActivity(){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}

