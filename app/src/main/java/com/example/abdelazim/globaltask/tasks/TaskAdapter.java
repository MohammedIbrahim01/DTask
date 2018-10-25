package com.example.abdelazim.globaltask.tasks;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.abdelazim.globaltask.R;
import com.example.abdelazim.globaltask.repository.model.Task;
import com.example.abdelazim.globaltask.utils.AppFormatter;

import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {


    private List<Task> taskList = new ArrayList<>();

    // Setter and Getter for taskList
    List<Task> getTaskList() {
        return taskList;
    }

    void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_tasks_list, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        // Get current task
        Task task = taskList.get(i);
        // Populate UI
        viewHolder.setTitle(task.getTitle());
        viewHolder.setDescription(task.getDescription());
        viewHolder.setTime(AppFormatter.formatTime(task.getTime()));
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }


    /**
     * Item ViewHolder
     */
    class ViewHolder extends RecyclerView.ViewHolder {
        TextView taskTitleTextView, taskDescriptionTextView, taskTimeTextView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            taskTitleTextView = itemView.findViewById(R.id.task_title_textView);
            taskDescriptionTextView = itemView.findViewById(R.id.task_description_textView);
            taskTimeTextView = itemView.findViewById(R.id.task_time_textView);
        }

        void setTitle(String title) {
            taskTitleTextView.setText(title);
        }

        void setDescription(String description) {
            taskDescriptionTextView.setText(description);
        }

        void setTime(String time) {
            taskTimeTextView.setText(time);
        }
    }
}
