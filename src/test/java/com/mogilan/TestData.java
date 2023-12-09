package com.mogilan;

import com.mogilan.dto.*;
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

    public static ClientDto getClientDto1() {
        return new ClientDto("Apple", "Corporate client with legal needs", null);
    }

    public static ClientDto getClientDto2() {
        return new ClientDto("Elon M.", "Individual seeking legal advice", null);
    }

    public static ClientDto getClientDto3() {
        return new ClientDto("Peter&Mike", "Startup company requiring legal services", null);
    }

    public static SimpleClientDto getSimpleClient1() {
        return new SimpleClientDto("Apple", "Corporate client with legal needs");
    }

    public static SimpleClientDto getSimpleClient2() {
        return new SimpleClientDto("Elon M.", "Individual seeking legal advice");
    }

    public static SimpleClientDto getSimpleClient3() {
        return new SimpleClientDto("Peter&Mike", "Startup company requiring legal services");
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

    public static TaskDto getTaskDto1() {
        return new TaskDto("Task 1", "Review contract", TaskPriority.HIGH, TaskStatus.ACCEPTED,
                LocalDate.of(2023, 1, 5), LocalDate.of(2023, 1, 15), null,
                5.0);
    }

    public static TaskDto getTaskDto2() {
        return new TaskDto("Task 2", "Legal research", TaskPriority.MEDIUM, TaskStatus.IN_PROGRESS,
                LocalDate.of(2023, 2, 1), LocalDate.of(2023, 2, 10), null,
                8.0);
    }

    public static TaskDto getTaskDto3() {
        return new TaskDto("Task 3", "Draft legal documents", TaskPriority.LOW, TaskStatus.RECEIVED,
                LocalDate.of(2023, 3, 15), LocalDate.of(2023, 3, 25),
                LocalDate.of(2023, 3, 20),
                10.0);
    }

    public static SimpleTaskDto getSimpleTask1() {
        return new SimpleTaskDto("Task 1", "Review contract", TaskPriority.HIGH, TaskStatus.ACCEPTED,
                LocalDate.of(2023, 1, 5), LocalDate.of(2023, 1, 15), null,
                5.0);
    }

    public static SimpleTaskDto getSimpleTask2() {
        return new SimpleTaskDto("Task 2", "Legal research", TaskPriority.MEDIUM, TaskStatus.IN_PROGRESS,
                LocalDate.of(2023, 2, 1), LocalDate.of(2023, 2, 10), null,
                8.0);
    }

    public static SimpleTaskDto getSimpleTask3() {
        return new SimpleTaskDto("Task 3", "Draft legal documents", TaskPriority.LOW, TaskStatus.RECEIVED,
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

    public static LawyerDto getLawyerDto1(){
        return new LawyerDto("John", "Doe", JobTitle.ASSOCIATE, 100.0);
    }

    public static LawyerDto getLawyerDto2(){
        return new LawyerDto("Max", "Smith", JobTitle.PARTNER, 200.0);
    }

    public static LawyerDto getLawyerDto3(){
        return new LawyerDto("Julia", "Johnson", JobTitle.MANAGING_PARTNER, 300.0);
    }
    public static SimpleLawyerDto getSimpleLawyer1(){
        return new SimpleLawyerDto("John", "Doe", JobTitle.ASSOCIATE, 100.0);
    }

    public static SimpleLawyerDto getSimpleLawyer2(){
        return new SimpleLawyerDto("Max", "Smith", JobTitle.PARTNER, 200.0);
    }

    public static SimpleLawyerDto getSimpleLawyer3(){
        return new SimpleLawyerDto("Julia", "Johnson", JobTitle.MANAGING_PARTNER, 300.0);
    }
}
