package com.example.abdelazim.globaltask.repository.remote;

import com.example.abdelazim.globaltask.repository.model.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RemoteDatabase {

    DatabaseReference tasksNode = FirebaseDatabase.getInstance().getReference().child("tasks");

    public void publishTask(Task task) {
        tasksNode.push().setValue(task);
    }
}
