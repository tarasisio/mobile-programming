package com.hillal.taskmanager.bottomSheetFragment;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.hillal.taskmanager.R;
import com.hillal.taskmanager.database.DatabaseClient;
import com.hillal.taskmanager.model.Task;

public class CreateSubTaskBottomSheetFragment extends BottomSheetDialogFragment {

    private int taskId; // Declare task ID variable
    private EditText addSubTaskTitle;
    private EditText addSubTaskDescription;
    private EditText subtaskTime;
    private Button addSubTask;

    boolean isEdit;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate your layout for the bottom sheet fragment
        View view = inflater.inflate(R.layout.fragment_subtask, container, false);

        // Initialize your views
        addSubTaskTitle = view.findViewById(R.id.addSubTaskTitle);
        addSubTaskDescription = view.findViewById(R.id.addSubTaskDescription);
        subtaskTime = view.findViewById(R.id.SubtaskTime);
        addSubTask = view.findViewById(R.id.addSubTask);

        // Set click listener for addSubTask button
        addSubTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Validate fields before adding subtask
                if(validateFields()) {
                    // Proceed with adding the subtask
                    addSubTask();
                }
            }
        });

        return view;
    }

    // Method to set the task ID
    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    // Method to validate fields
    private boolean validateFields() {
        if(TextUtils.isEmpty(addSubTaskTitle.getText())) {
            showToast("Please enter a subtask title");
            return false;
        } else if(TextUtils.isEmpty(addSubTaskDescription.getText())) {
            showToast("Please enter a subtask description");
            return false;
        } else if(TextUtils.isEmpty(subtaskTime.getText())) {
            showToast("Please enter subtask time");
            return false;
        }
        return true;
    }

    // Method to display a Toast message
    private void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    // Method to add subtask
    private void addSubTask() {
        // Implement your logic to add the subtask here



        class saveTaskInBackend extends AsyncTask<Void, Void, Void> {
            @SuppressLint("WrongThread")
            @Override
            protected Void doInBackground(Void... voids) {
                Task createTask = new Task();
                createTask.setTaskTitle(addSubTaskTitle.getText().toString());
                createTask.setTaskDescription(addSubTaskDescription.getText().toString());
                createTask.setLastAlarm(subtaskTime.getText().toString());


                if (!isEdit)
                    DatabaseClient.getInstance(getActivity()).getAppDatabase()
                            .dataBaseAction()
                            .insertDataIntoTaskList(createTask);
                else
                    DatabaseClient.getInstance(getActivity()).getAppDatabase()
                            .dataBaseAction()
                            .updateAnExistingRowSubTask(taskId, addSubTaskTitle.getText().toString(),
                                    addSubTaskDescription.getText().toString(),
                                    subtaskTime.getText().toString());

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    createAnAlarm();
                }
                setRefreshListener.refresh();
                Toast.makeText(getActivity(), "Your event is been added", Toast.LENGTH_SHORT).show();
                dismiss();

            }
        }
        saveTaskInBackend st = new saveTaskInBackend();
        st.execute();

    }


}
