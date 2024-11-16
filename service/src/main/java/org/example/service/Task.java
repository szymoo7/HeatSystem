package org.example.service;

public enum Task {

    NONE("No task"),
    CHECK1("Check1"),
    CHECK_TEMPERATURE("Check the temperature");

    final String taskDescription;

    Task(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    @Override
    public String toString() {
        return taskDescription;
    }
}
