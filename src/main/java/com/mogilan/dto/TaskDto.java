package com.mogilan.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class TaskDto {
    private Long id;
    private String title;
    private String description;
    private TaskPriority priority;
    private TaskStatus status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate receiptDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dueDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate completionDate;
    private double hoursSpentOnTask;
    private SimpleClientDto client;
    private List<SimpleLawyerDto> lawyers;

    public TaskDto() {
    }

    public TaskDto(String title, String description, TaskPriority priority, TaskStatus status, LocalDate receiptDate, LocalDate dueDate, LocalDate completionDate, double hoursSpentOnTask) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.status = status;
        this.receiptDate = receiptDate;
        this.dueDate = dueDate;
        this.completionDate = completionDate;
        this.hoursSpentOnTask = hoursSpentOnTask;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskPriority getPriority() {
        return priority;
    }

    public void setPriority(TaskPriority priority) {
        this.priority = priority;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public LocalDate getReceiptDate() {
        return receiptDate;
    }

    public void setReceiptDate(LocalDate receiptDate) {
        this.receiptDate = receiptDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDate getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(LocalDate completionDate) {
        this.completionDate = completionDate;
    }

    public double getHoursSpentOnTask() {
        return hoursSpentOnTask;
    }

    public void setHoursSpentOnTask(double hoursSpentOnTask) {
        this.hoursSpentOnTask = hoursSpentOnTask;
    }

    public SimpleClientDto getClient() {
        return client;
    }

    public void setClient(SimpleClientDto client) {
        this.client = client;
    }

    public List<SimpleLawyerDto> getLawyers() {
        return lawyers;
    }

    public void setLawyers(List<SimpleLawyerDto> lawyers) {
        this.lawyers = lawyers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskDto taskDto = (TaskDto) o;
        return Double.compare(taskDto.getHoursSpentOnTask(), getHoursSpentOnTask()) == 0 && Objects.equals(getId(), taskDto.getId()) && Objects.equals(getTitle(), taskDto.getTitle()) && Objects.equals(getDescription(), taskDto.getDescription()) && getPriority() == taskDto.getPriority() && getStatus() == taskDto.getStatus() && Objects.equals(getReceiptDate(), taskDto.getReceiptDate()) && Objects.equals(getDueDate(), taskDto.getDueDate()) && Objects.equals(getCompletionDate(), taskDto.getCompletionDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTitle(), getDescription(), getPriority(), getStatus(), getReceiptDate(), getDueDate(), getCompletionDate(), getHoursSpentOnTask());
    }
}
