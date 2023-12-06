package com.mogilan.dto;

import java.time.LocalDate;
import java.util.List;

public class TaskDto {
    private Long id;
    private String title;
    private String description;
    private TaskPriority priority;
    private TaskStatus status;
    private LocalDate receiptDate;
    private LocalDate dueDate;
    private ClientDto client;
    private List<LawyerDto> lawyers;
}
