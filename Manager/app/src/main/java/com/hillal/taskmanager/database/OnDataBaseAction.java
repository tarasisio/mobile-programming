package com.hillal.taskmanager.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.hillal.taskmanager.model.Task;
import com.hillal.taskmanager.model.SubTask;
import java.util.List;

@Dao
public interface OnDataBaseAction {

    @Query("SELECT * FROM Task")
    List<Task> getAllTasksList();

    @Query("SELECT * FROM SubTask")
    List<SubTask> getAllSubTasksList();

    @Insert
    void insertDataIntoSubTaskList(SubTask subTask);

    @Query("DELETE FROM Task")
    void truncateTheList();


    @Query("DELETE FROM SubTask")
    void truncateTheSubList();

    @Insert
    void insertDataIntoTaskList(Task task);


    @Query("DELETE FROM Task WHERE taskId = :taskId")
    void deleteTaskFromId(int taskId);


    @Query("DELETE FROM SubTask WHERE taskId = :taskId")
    void deleteSubTaskFromId(int taskId);

    @Query("SELECT * FROM Task WHERE taskId = :taskId")
    Task selectDataFromAnId(int taskId);



    @Query("SELECT * FROM SubTask WHERE taskId = :taskId")
    SubTask selectSubDataFromAnId(int taskId);

    @Query("UPDATE Task SET taskTitle = :taskTitle, taskDescription = :taskDescription, date = :taskDate, " +
            "lastAlarm = :taskTime, event = :taskEvent WHERE taskId = :taskId")
    void updateAnExistingRow(int taskId, String taskTitle, String taskDescription , String taskDate, String taskTime,
                             String taskEvent);



    @Query("UPDATE SubTask SET SubtaskTitle = :SubtaskTitle, SubtaskDescription = :SubtaskDescription, " +
            "firstAlarmTime = :SubtaskTime WHERE taskId = :taskId")
    void updateAnExistingRowSubTask(int taskId, String SubtaskTitle, String SubtaskDescription ,  String SubtaskTime);

    @Query("SELECT * FROM Task WHERE datetime(date || ' ' || lastAlarm) < datetime('now')")
    List<Task> getAllPendingTasks();


}
