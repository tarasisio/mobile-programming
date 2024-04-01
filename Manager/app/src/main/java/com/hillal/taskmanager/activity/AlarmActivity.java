package com.hillal.taskmanager.activity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.hillal.taskmanager.R;

//import butterknife.BindView;
//import butterknife.ButterKnife;

public class AlarmActivity extends BaseActivity {


    private static AlarmActivity inst;
//    @BindView(R.id.imageView)
    ImageView imageView=findViewById(R.id.imageView);


//    @BindView(R.id.title)
    TextView title= findViewById(R.id.title);


//    @BindView(R.id.description)
    TextView description=findViewById(R.id.description);


//    @BindView(R.id.timeAndData)
    TextView timeAndData =findViewById(R.id.timeAndData);



//    @BindView(R.id.closeButton)
    Button closeButton =findViewById(R.id.closeButton);
    MediaPlayer mediaPlayer;

    public static AlarmActivity instance() {
        return inst;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
//        ButterKnife.bind(this);

        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.notification);
        mediaPlayer.start();

        if(getIntent().getExtras() != null) {
            title.setText(getIntent().getStringExtra("TITLE"));
            description.setText(getIntent().getStringExtra("DESC"));
            timeAndData.setText(getIntent().getStringExtra("DATE") + ", " + getIntent().getStringExtra("TIME"));
        }

        Glide.with(getApplicationContext()).load(R.drawable.alert).into(imageView);
        closeButton.setOnClickListener(view -> finish());

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.release();
    }
}
