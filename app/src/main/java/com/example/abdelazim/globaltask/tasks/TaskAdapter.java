package com.example.abdelazim.globaltask.tasks;

import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.abdelazim.globaltask.R;
import com.example.abdelazim.globaltask.repository.model.Task;
import com.example.abdelazim.globaltask.utils.AppFormatter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {


    private List<Task> taskList = new ArrayList<>();
    private final OnTaskItemClickListener mOnTaskItemClickListener;

    public TaskAdapter(OnTaskItemClickListener mOnTaskItemClickListener) {
        this.mOnTaskItemClickListener = mOnTaskItemClickListener;
    }


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
        if (task.isLate()) {
            viewHolder.markAsLate();
            Log.i("WWLW", "taskId: " + task.getId() + "late: " + task.isLate());
        }
        Log.i("WWLW", "taskId: " + task.getId() + "late: " + task.isLate());
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }


    /**
     * Item ViewHolder
     */
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView taskTitleTextView;
        TextView taskDescriptionTextView;
        TextView taskTimeTextView;
        LinearLayout rootView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            taskTitleTextView = itemView.findViewById(R.id.task_title_textView);
            taskDescriptionTextView = itemView.findViewById(R.id.task_description_textView);
            taskTimeTextView = itemView.findViewById(R.id.task_time_textView);
            rootView = itemView.findViewById(R.id.task_item_root_view);
            itemView.setOnClickListener(this);
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

        void markAsLate() {
            rootView.getBackground().setColorFilter(new PorterDuffColorFilter(rootView.getResources().getColor(R.color.colorBgLateTask), PorterDuff.Mode.MULTIPLY));
        }

        @Override
        public void onClick(View v) {
            mOnTaskItemClickListener.onTaskItemClick(getAdapterPosition());
        }
    }

    public interface OnTaskItemClickListener {
        void onTaskItemClick(int index);
    }
}
