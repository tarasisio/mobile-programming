package com.hillal.taskmanager.model;

import androidx.room.ColumnInfo;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.Entity;

import java.io.Serializable;


@Entity
public class SubTask implements Serializable {



    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "taskId")
    public
    int taskId;
    @ColumnInfo(name = "SubtaskTitle")
    public
    String SubtaskTitle;

    @ColumnInfo(name = "SubtaskDescription")
    public
    String SubtaskDescription;
    @ColumnInfo(name = "isComplete")
    public
    boolean isComplete;
    @ColumnInfo(name = "firstAlarmTime")
    public
    String firstAlarmTime;
    @ColumnInfo(name = "secondAlarmTime")
    public
    String secondAlarmTime;
    @ColumnInfo(name = "lastAlarm")
    public
    String lastAlarm;



    public SubTask() {

    }

    public boolean isSubComplete() {
        return isComplete;
    }


    public void setSubComplete(boolean complete) {
        isComplete = complete;
    }

    public String getFirstSubAlarmTime() {
        return firstAlarmTime;
    }

    public void setFirstSubAlarmTime(String firstAlarmTime) {
        this.firstAlarmTime = firstAlarmTime;
    }

    public String getSecondSubAlarmTime() {
        return secondAlarmTime;
    }

    public void setSecondSubAlarmTime(String secondAlarmTime) {
        this.secondAlarmTime = secondAlarmTime;
    }

    public String getSubLastAlarm() {
        return lastAlarm;
    }

    public void setSubLastAlarm(String lastAlarm) {
        this.lastAlarm = lastAlarm;
    }

    public int getSubTaskId() {
        return taskId;
    }

    public void setSubTaskId(int taskId) {
        this.taskId = taskId;
    }

    public  String getSubTaskTitle() {
        return SubtaskTitle;
    }

    public void setSubTaskTitle(String SubtaskTitle) {
        this.SubtaskTitle = SubtaskTitle;
    }





    public String getSubTaskDescription() {
        return SubtaskDescription;
    }

    public void setSubTaskDescription(String SubtaskDescrption) {
        this.SubtaskDescription = SubtaskDescrption;
    }

}