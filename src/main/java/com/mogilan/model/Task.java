package com.mogilan.model;

import com.mogilan.dto.TaskPriority;
import com.mogilan.dto.TaskStatus;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "tasks", schema = "public")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    @Enumerated(EnumType.STRING)
    private TaskPriority priority;
    @Enumerated(EnumType.STRING)
    private TaskStatus status;
    private LocalDate receiptDate;
    private LocalDate dueDate;
    private LocalDate completionDate;
    private double hoursSpentOnTask;
    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;
    @ManyToMany
    @JoinTable(name = "lawyers_tasks",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "lawyer_id"))
    private List<Lawyer> lawyers;

    public Task() {
    }

    public Task(String title, String description, TaskPriority priority, TaskStatus status, LocalDate receiptDate, LocalDate dueDate, LocalDate completionDate, double hoursSpentOnTask) {
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

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public List<Lawyer> getLawyers() {
        return lawyers;
    }

    public void setLawyers(List<Lawyer> lawyers) {
        this.lawyers = lawyers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Double.compare(task.getHoursSpentOnTask(), getHoursSpentOnTask()) == 0 && Objects.equals(getId(), task.getId()) && Objects.equals(getTitle(), task.getTitle()) && Objects.equals(getDescription(), task.getDescription()) && getPriority() == task.getPriority() && getStatus() == task.getStatus() && Objects.equals(getReceiptDate(), task.getReceiptDate()) && Objects.equals(getDueDate(), task.getDueDate()) && Objects.equals(getCompletionDate(), task.getCompletionDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTitle(), getDescription(), getPriority(), getStatus(), getReceiptDate(), getDueDate(), getCompletionDate(), getHoursSpentOnTask());
    }
}
