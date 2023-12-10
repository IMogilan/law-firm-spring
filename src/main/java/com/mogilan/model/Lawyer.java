package com.mogilan.model;

import com.mogilan.dto.JobTitle;
import jakarta.persistence.*;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "lawyers", schema = "public")
public class Lawyer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "first_name", nullable = false)
    private String firstName;
    @Column(name = "last_name", nullable = false)
    private String lastName;
    @Column(name = "job_title", nullable = false)
    @Enumerated(EnumType.STRING)
    private JobTitle jobTitle;
    @Column(name = "hourly_rate", nullable = false)
    private double hourlyRate;
    @ManyToMany
    @JoinTable(name = "lawyers_tasks",
    joinColumns = @JoinColumn(name = "lawyer_id"),
    inverseJoinColumns = @JoinColumn(name = "task_id"))
    private List<Task> tasks;

    public Lawyer() {
    }

    public Lawyer(String firstName, String lastName, JobTitle jobTitle, double hourlyRate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.jobTitle = jobTitle;
        this.hourlyRate = hourlyRate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public JobTitle getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(JobTitle jobTitle) {
        this.jobTitle = jobTitle;
    }

    public double getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lawyer lawyer = (Lawyer) o;
        return Double.compare(lawyer.getHourlyRate(), getHourlyRate()) == 0 && Objects.equals(getId(), lawyer.getId()) && Objects.equals(getFirstName(), lawyer.getFirstName()) && Objects.equals(getLastName(), lawyer.getLastName()) && getJobTitle() == lawyer.getJobTitle();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getFirstName(), getLastName(), getJobTitle(), getHourlyRate());
    }
}
