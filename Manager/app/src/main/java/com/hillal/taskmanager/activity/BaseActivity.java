package com.hillal.taskmanager.activity;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.hillal.taskmanager.R;
import com.hillal.taskmanager.bottomSheetFragment.CreateSubTaskBottomSheetFragment;

import java.util.Locale;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public abstract class BaseActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_task);

        // Find additional ImageView from item_task
        ImageView additional = findViewById(R.id.additional);
        additional.setOnClickListener(view -> {
            // Create an instance of the bottom sheet fragment
            CreateSubTaskBottomSheetFragment bottomSheetFragment = new CreateSubTaskBottomSheetFragment();

            // Use getSupportFragmentManager() to manage fragments in the activity
            // and show the bottom sheet fragment
            bottomSheetFragment.show(getSupportFragmentManager(), "bottom_sheet_fragment_tag");
        });
    }
    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }


}
