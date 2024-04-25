    package com.hillal.taskmanager.adapter;

    import android.annotation.SuppressLint;
    import android.app.AlertDialog;
    import android.app.Dialog;
    import android.content.Context;
    import android.content.DialogInterface;
    import android.graphics.Color;
    import android.graphics.drawable.ColorDrawable;
    import android.os.AsyncTask;
    import android.os.Build;
    import android.util.Log;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.Button;
    import android.widget.ImageView;
    import android.widget.PopupMenu;
    import android.widget.TextView;
    import android.widget.Toast;

    import androidx.annotation.NonNull;
    import androidx.recyclerview.widget.RecyclerView;

    import com.bumptech.glide.Glide;
    import com.hillal.taskmanager.R;
    import com.hillal.taskmanager.activity.MainActivity;
    import com.hillal.taskmanager.bottomSheetFragment.CreateTaskBottomSheetFragment;
    import com.hillal.taskmanager.database.DatabaseClient;
    import com.hillal.taskmanager.model.SubTask;
    import com.hillal.taskmanager.model.Task;

    import java.text.SimpleDateFormat;
    import java.util.Date;
    import java.util.List;
    import java.util.Locale;

    public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

        MainActivity activity;

        private OnAdditionalButtonClickListener additionalButtonClickListener;

        private MainActivity context;
        private LayoutInflater inflater;
        private List<Task> taskList;
        public SimpleDateFormat dateFormat = new SimpleDateFormat("EE dd MMM yyyy", Locale.US);
        public SimpleDateFormat inputDateFormat = new SimpleDateFormat("dd-M-yyyy", Locale.US);
        Date date = null;
        String outputDateString = null;
        CreateTaskBottomSheetFragment.setRefreshListener setRefreshListener;

        public TaskAdapter(MainActivity context, List<Task> taskList,  CreateTaskBottomSheetFragment.setRefreshListener setRefreshListener) {
            this.context = context;
            this.taskList = taskList;
            this.setRefreshListener = setRefreshListener;
            this.inflater = LayoutInflater.from(context);
        }

        @NonNull
        @Override
        public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
            View view = inflater.inflate(R.layout.item_task, viewGroup, false);
            return new TaskViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
            Task task = taskList.get(position);
            holder.title.setText(task.getTaskTitle());
            holder.description.setText(task.getTaskDescription());
            holder.time.setText(task.getLastAlarm());
            holder.status.setText(task.isComplete() ? "COMPLETED" : "UPCOMING");
            holder.options.setOnClickListener(view -> showPopUpMenu(view, position));


            // Set click listener for the additional button
            holder.additional.setOnClickListener(view -> {
                if (additionalButtonClickListener != null) {
                    additionalButtonClickListener.onAdditionalButtonClick(position);
                }else Log.i("TaskAdapter","Emplty.."+position);
            });

            try {
                date = inputDateFormat.parse(task.getDate());
                outputDateString = dateFormat.format(date);

                String[] items1 = outputDateString.split(" ");
                String day = items1[0];
                String dd = items1[1];
                String month = items1[2];

                holder.day.setText(day);
                holder.date.setText(dd);
                holder.month.setText(month);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        public  void subTaskAdittion(View view ,int position){
            Log.i("Task Adapter","The additional button has been clicked : "+position);


        }






        public void showPopUpMenu(View view, int position) {
            final Task task = taskList.get(position);
            PopupMenu popupMenu = new PopupMenu(context, view);
            popupMenu.getMenuInflater().inflate(R.menu.menu, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(item -> {
                int itemId = item.getItemId();
                if (itemId == R.id.menuDelete) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                    alertDialogBuilder
                            .setTitle(R.string.delete_confirmation)
                            .setMessage(R.string.sureToDelete)
                            .setPositiveButton(R.string.yes, (dialog, which) -> {
                                deleteTaskFromId(task.getTaskId(), position);
                            })
                            .setNegativeButton(R.string.no, (dialog, which) -> dialog.cancel());

                    // Set background color
                    Dialog dialog = alertDialogBuilder.create();
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLUE));

                    // Set button text color
                    dialog.setOnShowListener(dialogInterface -> {
                        Button positiveButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE);
                        Button negativeButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_NEGATIVE);
                        if (positiveButton != null && negativeButton != null) {
                            positiveButton.setTextColor(context.getResources().getColor(android.R.color.black)); // Change to the desired text color
                            negativeButton.setTextColor(context.getResources().getColor(android.R.color.black)); // Change to the desired text color
                        }
                    });

                    dialog.show();
                    return true; // Item click handled
                } else if (itemId == R.id.menuUpdate) {
                    CreateTaskBottomSheetFragment createTaskBottomSheetFragment = new CreateTaskBottomSheetFragment();
                    createTaskBottomSheetFragment.setTaskId(task.getTaskId(), true, (CreateTaskBottomSheetFragment.setRefreshListener) context, context);
                    createTaskBottomSheetFragment.show(context.getSupportFragmentManager(), createTaskBottomSheetFragment.getTag());
                    return true; // Item click handled
                } else if (itemId == R.id.menuComplete) {
                    AlertDialog.Builder completeAlertDialog = new AlertDialog.Builder(context);
                    completeAlertDialog
                            .setTitle(R.string.confirmation)
                            .setMessage(R.string.sureToMarkAsComplete)
                            .setPositiveButton(R.string.yes, (dialog, which) -> showCompleteDialog(task.getTaskId(), position))
                            .setNegativeButton(R.string.no, (dialog, which) -> dialog.cancel());

                    // Set background color
                    Dialog dialog = completeAlertDialog.create();
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                    // Set button text color
                    dialog.setOnShowListener(dialogInterface -> {
                        Button positiveButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE);
                        Button negativeButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_NEGATIVE);
                        if (positiveButton != null && negativeButton != null) {
                            positiveButton.setTextColor(context.getResources().getColor(android.R.color.black)); // Change to the desired text color
                            negativeButton.setTextColor(context.getResources().getColor(android.R.color.black)); // Change to the desired text color
                        }
                    });

                    dialog.show();
                    return true; // Item click handled
                }
                return false; // Item click not handled
            });
            popupMenu.show();
        }

        public void showCompleteDialog(int taskId, int position) {
            Dialog dialog = new Dialog(context, R.style.AppTheme);
            dialog.setContentView(R.layout.dialog_completed_theme);
            Button close = dialog.findViewById(R.id.closeButton);
            close.setOnClickListener(view -> {
                deleteTaskFromId(taskId, position);
                dialog.dismiss();
            });
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.show();
        }


        private void deleteTaskFromId(int taskId, int position) {
            class GetSavedTasks extends AsyncTask<Void, Void, List<Task>> {
                @Override
                protected List<Task> doInBackground(Void... voids) {
                    DatabaseClient.getInstance(context)
                            .getAppDatabase()
                            .dataBaseAction()
                            .deleteTaskFromId(taskId);

                    return taskList;
                }

                @Override
                protected void onPostExecute(List<Task> tasks) {
                    super.onPostExecute(tasks);
                    removeAtPosition(position);
                    setRefreshListener.refresh();
                }


            }
            GetSavedTasks savedTasks = new GetSavedTasks();
            savedTasks.execute();
        }

        private void removeAtPosition(int position) {
            taskList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, taskList.size());
        }

        @Override
        public int getItemCount() {
            return taskList.size();
        }





        public class TaskViewHolder extends RecyclerView.ViewHolder {
            TextView day;
            TextView date;
            TextView month;
            TextView title;
            TextView description;
            TextView status;
            ImageView options,additional;
            TextView time;






            public TaskViewHolder(@NonNull View itemView) {
                super(itemView);
                day = itemView.findViewById(R.id.day);
                date = itemView.findViewById(R.id.date);
                month = itemView.findViewById(R.id.month);
                title = itemView.findViewById(R.id.title);
                description = itemView.findViewById(R.id.description);
                status = itemView.findViewById(R.id.status);
                options = itemView.findViewById(R.id.options);
                additional = itemView.findViewById(R.id.additional);
                time = itemView.findViewById(R.id.time);




            }

        }





        // Modify TaskAdapter to include a method for updating the dataset
        public void updateData(List<Task> newTasks) {
            newTasks.clear();
            newTasks.addAll(newTasks);
            notifyDataSetChanged();
        }







        // Interface for handling additional button clicks
        public interface OnAdditionalButtonClickListener {
            void onAdditionalButtonClick(int position);
        }

        // Method to set the click listener for the additional button
        public void setOnAdditionalButtonClickListener(OnAdditionalButtonClickListener listener) {
            this.additionalButtonClickListener = listener;
        }






    }

