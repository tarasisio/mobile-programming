package com.hillal.taskmanager.broadcastReceiver;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.hillal.taskmanager.R;
import com.hillal.taskmanager.activity.MainActivity;
import com.hillal.taskmanager.database.DatabaseClient;
import com.hillal.taskmanager.model.Task;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AlarmService extends Service {
    private static final long CHECK_INTERVAL = 30 * 1000; // Check interval: 30 seconds
    private static final int NOTIFICATION_ID = 3;
    private MediaPlayer mediaPlayer;
    private Handler handler;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startAlarmCheck();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopAlarmCheck();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void startAlarmCheck() {
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                checkPendingAlarms();
                handler.postDelayed(this, CHECK_INTERVAL);
            }
        }, CHECK_INTERVAL);
    }

    private void stopAlarmCheck() {
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }

    private void checkPendingAlarms() {
        try {
            // Retrieve all pending alarms from the database
            List<Task> pendingTasks = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                    .dataBaseAction().getAllPendingTasks();

            if (pendingTasks.isEmpty()) {
                // No pending tasks found
                Log.d("AlarmService", "No pending tasks found.");
            } else {
                // Pending tasks found
                Log.d("AlarmService", "Pending tasks found:");
                for (Task task : pendingTasks) {
                    Log.d("AlarmService", "Task: " + task.getTaskTitle() + " - Date: " + task.getDate() + " - Time: " + task.getLastAlarm());
                }

                // Get current time
                long currentTime = System.currentTimeMillis();

                // Iterate through pending alarms
                for (Task task : pendingTasks) {
                    try {
                        // Parse task time
                        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                        Date alarmTime = format.parse(task.getDate() + " " + task.getLastAlarm());
                        long alarmTimeInMillis = alarmTime.getTime();

                        // Calculate time difference
                        long timeDifference = alarmTimeInMillis - currentTime;

                        // Define the threshold for sending notification in advance (e.g., 30 seconds)
                        long threshold = 30 * 1000;

                        // If the difference is within the threshold, send notification in advance
                        if (timeDifference <= threshold) {
                            sendNotification();
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private void sendNotification() {
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Define the notification channel
            String channelId = "task_reminders";
            CharSequence channelName = "Task Reminders";
            String channelDescription = "Get reminders for your tasks";

            int importance = NotificationManager.IMPORTANCE_HIGH; // High importance for task reminders

            NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
            channel.setDescription(channelDescription);

            // Register the notification channel with the system
            mNotificationManager.createNotificationChannel(channel);
        }

        // Now you can use this channel when creating notifications
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "task_reminders")
                .setSmallIcon(R.mipmap.ic_launcher) // notification icon
                .setContentTitle("Task Reminder") // title for notification
                .setContentText("Don't forget to complete your task!") // message for notification
                .setAutoCancel(true); // clear notification after click

        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pi);
        mNotificationManager.notify(0, mBuilder.build());
    }
}
