package com.hillal.taskmanager.activity;

import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hillal.taskmanager.R;
import com.hillal.taskmanager.adapter.TaskAdapter;
import com.hillal.taskmanager.bottomSheetFragment.CreateSubTaskBottomSheetFragment;
import com.hillal.taskmanager.bottomSheetFragment.CreateTaskBottomSheetFragment;
import com.hillal.taskmanager.bottomSheetFragment.ShowCalendarViewBottomSheet;
import com.hillal.taskmanager.broadcastReceiver.AlarmBroadcastReceiver;
import com.hillal.taskmanager.database.DatabaseClient;
import com.hillal.taskmanager.model.Task;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements CreateTaskBottomSheetFragment.setRefreshListener, TaskAdapter.OnAdditionalButtonClickListener  {

    RecyclerView taskRecycler;
    TextView addTask;
    TaskAdapter taskAdapter;
    List<Task> tasks = new ArrayList<>();
    ImageView noDataImage;
    ImageView calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        taskRecycler = findViewById(R.id.taskRecycler);
        addTask = findViewById(R.id.addTask);
        noDataImage = findViewById(R.id.noDataImage);
        calendar = findViewById(R.id.calendar);

        setUpAdapter();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        ComponentName receiver = new ComponentName(this, AlarmBroadcastReceiver.class);
        PackageManager pm = getPackageManager();
        pm.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
        Glide.with(getApplicationContext()).load(R.drawable.first_note).into(noDataImage);

        addTask.setOnClickListener(view -> {
            CreateTaskBottomSheetFragment createTaskBottomSheetFragment = new CreateTaskBottomSheetFragment();
            createTaskBottomSheetFragment.setTaskId(0, false, this, MainActivity.this);
            createTaskBottomSheetFragment.show(getSupportFragmentManager(), createTaskBottomSheetFragment.getTag());
        });


        // Set click listener for additional button in TaskAdapter
        taskAdapter.setOnAdditionalButtonClickListener(this);

        getSavedTasks();

        calendar.setOnClickListener(view -> {
            ShowCalendarViewBottomSheet showCalendarViewBottomSheet = new ShowCalendarViewBottomSheet();
            showCalendarViewBottomSheet.show(getSupportFragmentManager(), showCalendarViewBottomSheet.getTag());
        });



    }


    // Method to handle additional button click
    @Override
    public void onAdditionalButtonClick(int position) {

    }

    // Method to open the fragment for adding a subtask
    private void openAddSubTaskFragment(int position) {
        CreateSubTaskBottomSheetFragment fragment = new CreateSubTaskBottomSheetFragment();
        // Pass any necessary data to the fragment
        fragment.setTaskId(tasks.get(position).getTaskId()); // Example: Pass task ID to the fragment
        fragment.show(getSupportFragmentManager(), "add_subtask_fragment");
    }

    public void setUpAdapter() {
        taskAdapter = new TaskAdapter(this, tasks, this);
        taskAdapter.setOnAdditionalButtonClickListener(new TaskAdapter.OnAdditionalButtonClickListener() {
            @Override
            public void onAdditionalButtonClick(int position) {
                openAddSubTaskFragment(position);
            }
        });

        taskRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        taskRecycler.setAdapter(taskAdapter);
    }

    private void getSavedTasks() {

        class GetSavedTasks extends AsyncTask<Void, Void, List<Task>> {
            @Override
            protected List<Task> doInBackground(Void... voids) {
                tasks = DatabaseClient
                        .getInstance(getApplicationContext())
                        .getAppDatabase()
                        .dataBaseAction()
                        .getAllTasksList();
                return tasks;
            }

            @Override
            protected void onPostExecute(List<Task> tasks) {
                super.onPostExecute(tasks);
                noDataImage.setVisibility(tasks.isEmpty() ? View.VISIBLE : View.GONE);
                setUpAdapter();
            }
        }

        GetSavedTasks savedTasks = new GetSavedTasks();
        savedTasks.execute();
    }



    @Override
    public void refresh() {
        getSavedTasks();
    }

}
