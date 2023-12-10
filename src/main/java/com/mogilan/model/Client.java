package com.mogilan.model;

import jakarta.persistence.*;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "clients", schema = "public")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "name", unique = true, nullable = false)
    private String name;
    @Column(name = "description", unique = true)
    private String description;
    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.REMOVE}, mappedBy = "client")
    private List<Task> tasks;

    public Client() {
    }

    public Client(String name, String description, List<Task> tasks) {
        this.name = name;
        this.description = description;
        this.tasks = tasks;
    }

    public Client(Long id, String name, String description, List<Task> tasks) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.tasks = tasks;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
        Client client = (Client) o;
        return Objects.equals(getId(), client.getId()) && Objects.equals(getName(), client.getName()) && Objects.equals(getDescription(), client.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getDescription());
    }
}
