package com.mogilan;

import com.mogilan.dto.JobTitle;
import com.mogilan.dto.TaskPriority;
import com.mogilan.dto.TaskStatus;
import com.mogilan.model.Client;
import com.mogilan.model.Lawyer;
import com.mogilan.model.Task;

import java.time.LocalDate;

public final class TestData {

    private TestData() {
    }

    public static Client getClient1() {
        return new Client("Apple", "Corporate client with legal needs", null);
    }

    public static Client getClient2() {
        return new Client("Elon M.", "Individual seeking legal advice", null);
    }

    public static Client getClient3() {
        return new Client("Peter&Mike", "Startup company requiring legal services", null);
    }

    public static Task getTask1() {
        return new Task("Task 1", "Review contract", TaskPriority.HIGH, TaskStatus.ACCEPTED,
                LocalDate.of(2023, 1, 5), LocalDate.of(2023, 1, 15), null,
                5.0);
    }

    public static Task getTask2() {
        return new Task("Task 2", "Legal research", TaskPriority.MEDIUM, TaskStatus.IN_PROGRESS,
                LocalDate.of(2023, 2, 1), LocalDate.of(2023, 2, 10), null,
                8.0);
    }

    public static Task getTask3() {
        return new Task("Task 3", "Draft legal documents", TaskPriority.LOW, TaskStatus.RECEIVED,
                LocalDate.of(2023, 3, 15), LocalDate.of(2023, 3, 25),
                LocalDate.of(2023, 3, 20),
                10.0);
    }

    public static Lawyer getLawyer1(){
        return new Lawyer("John", "Doe", JobTitle.ASSOCIATE, 100.0);
    }

    public static Lawyer getLawyer2(){
        return new Lawyer("Max", "Smith", JobTitle.PARTNER, 200.0);
    }

    public static Lawyer getLawyer3(){
        return new Lawyer("Julia", "Johnson", JobTitle.MANAGING_PARTNER, 300.0);
    }
}
