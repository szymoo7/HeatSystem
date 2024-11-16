package org.example.service;

public class TaskInfo {
    private final String executor;
    private final String task;
    private final String taskDescription;
    private final String taskStatus;
    private final String dueDate;
    private final String assignedDate;

    public TaskInfo(String executor, String task, String taskDescription, String taskStatus, String dueDate, String assignedDate) {
        this.executor = executor;
        this.task = task;
        this.taskDescription = taskDescription;
        this.taskStatus = taskStatus;
        this.dueDate = dueDate;
        this.assignedDate = assignedDate;
    }

    public String getExecutor() {
        return executor;
    }

    public String getTask() {
        return task;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public String getDueDate() {
        return dueDate;
    }

    public String getAssignedDate() {
        return assignedDate;
    }
}
