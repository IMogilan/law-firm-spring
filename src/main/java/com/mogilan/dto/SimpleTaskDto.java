package com.mogilan.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class SimpleTaskDto {
    private Long id;
    private String title;
    private String description;
    private TaskPriority priority;
    private TaskStatus status;
    private LocalDate receiptDate;
    private LocalDate dueDate;
    private LocalDate completionDate;
    private double hoursSpentOnTask;
}
