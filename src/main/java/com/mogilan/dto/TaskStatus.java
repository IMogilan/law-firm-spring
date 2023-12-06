package com.mogilan.dto;

public enum TaskStatus {
    UPCOMING("There is information that the client is going to assign a task."),
    RECEIVED("The task was received from the client but has not been accepted yet."),
    ACCEPTED("The task was accepted but has not started executing yet."),
    DECLINED("The task was not accepted."),
    IN_PROGRESS("The task has been transferred to the responsible person and is being completed."),
    PENDING("The task has been transferred to the responsible person but need to pause work temporarily."),
    OVERDUE("The deadline for completion has passed."),
    CANCELED("The client canceled the task after it was accepted."),
    COMPLETED("The task was successfully completed (including partially if the client canceled the task independently without our fault)");

    private final String description;

    TaskStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
