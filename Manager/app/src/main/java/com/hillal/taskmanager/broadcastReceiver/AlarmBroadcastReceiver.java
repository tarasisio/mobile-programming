package com.hillal.taskmanager.broadcastReceiver;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.hillal.taskmanager.R;
import com.hillal.taskmanager.activity.AlarmActivity;


public class AlarmBroadcastReceiver extends BroadcastReceiver {



    @SuppressLint("MissingPermission")
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        // Handle alarm trigger action
        if ("YOUR_CUSTOM_ALARM_ACTION".equals(action)) {
            String title = intent.getStringExtra("TITLE");
            String desc = intent.getStringExtra("DESC");
            String date = intent.getStringExtra("DATE");
            String time = intent.getStringExtra("TIME");

            long alarmTime = intent.getLongExtra("ALARM_TIME", 0);
            long currentTime = System.currentTimeMillis();

            // Calculate the difference between current time and alarm time
            long timeDifference = alarmTime - currentTime;

            // Define the threshold for starting the AlarmService (e.g., 30 seconds)
            long threshold = 30 * 1000;

            // If the difference is within the threshold, start the AlarmService
            if (timeDifference <= threshold) {
                // Start the AlarmService to play the alarm sound
                Intent serviceIntent = new Intent(context, AlarmService.class);
                serviceIntent.putExtras(intent.getExtras());
                context.startService(serviceIntent);
            }

            // Display notification
            NotificationCompat.Builder notification = new NotificationCompat.Builder(context, "tasks_reminders")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(title)
                    .setContentText(desc)
                    .setPriority(NotificationCompat.PRIORITY_HIGH);

            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
            notificationManagerCompat.notify(200, notification.build());
            Toast.makeText(context, "Alarm triggered, showing notification...", Toast.LENGTH_SHORT).show();

            // Start AlarmActivity
            Intent alarmActivityIntent = new Intent(context, AlarmActivity.class);
            alarmActivityIntent.putExtras(intent.getExtras());
            alarmActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(alarmActivityIntent);

        }
    }
}
