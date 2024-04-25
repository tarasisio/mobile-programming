package com.hillal.taskmanager.bottomSheetFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.hillal.taskmanager.R;

public class CreateSubTaskBottomSheetFragment extends BottomSheetDialogFragment {


    private int taskId; // Declare task ID variable
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate your layout for the bottom sheet fragment
        return inflater.inflate(R.layout.fragment_subtask, container, false);
    }


    // Method to set the task ID
    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }
}
