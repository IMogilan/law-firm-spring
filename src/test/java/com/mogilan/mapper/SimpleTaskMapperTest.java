package com.mogilan.mapper;

import com.mogilan.TestData;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SimpleTaskMapperTest {

    SimpleTaskMapper simpleTaskMapper = SimpleTaskMapper.INSTANCE;

    @Test
    void toSimpleTaskDtoSuccess() {
        var task1 = TestData.getTask1();
        var task2 = TestData.getTask2();
        var task3 = TestData.getTask3();

        var actualResult1 = simpleTaskMapper.toSimpleTaskDto(task1);
        var actualResult2 = simpleTaskMapper.toSimpleTaskDto(task2);
        var actualResult3 = simpleTaskMapper.toSimpleTaskDto(task3);

        assertThat(actualResult1).isEqualTo(TestData.getSimpleTask1());
        assertThat(actualResult2).isEqualTo(TestData.getSimpleTask2());
        assertThat(actualResult3).isEqualTo(TestData.getSimpleTask3());
    }

    @Test
    void toTaskSuccess() {
        var simpleTask1 = TestData.getSimpleTask1();
        var simpleTask2 = TestData.getSimpleTask2();
        var simpleTask3 = TestData.getSimpleTask3();

        var actualResult1 = simpleTaskMapper.toTask(simpleTask1);
        var actualResult2 = simpleTaskMapper.toTask(simpleTask2);
        var actualResult3 = simpleTaskMapper.toTask(simpleTask3);

        assertThat(actualResult1).isEqualTo(TestData.getTask1());
        assertThat(actualResult2).isEqualTo(TestData.getTask2());
        assertThat(actualResult3).isEqualTo(TestData.getTask3());
    }

    @Test
    void toSimpleTaskDtoListSuccess() {
        var task1 = TestData.getTask1();
        var task2 = TestData.getTask2();
        var task3 = TestData.getTask3();

        var actualResult = simpleTaskMapper.toSimpleTaskDtoList(List.of(task1, task2, task3));

        assertThat(actualResult).isNotEmpty();
        assertThat(actualResult).hasSize(3);
        assertThat(actualResult).isEqualTo(List.of(TestData.getSimpleTask1(), TestData.getSimpleTask2(), TestData.getSimpleTask3()));
    }

    @Test
    void toTaskListSuccess() {
        var simpleTask1 = TestData.getSimpleTask1();
        var simpleTask2 = TestData.getSimpleTask2();
        var simpleTask3 = TestData.getSimpleTask3();

        var actualResult = simpleTaskMapper.toTaskList(List.of(simpleTask1, simpleTask2, simpleTask3));

        assertThat(actualResult).isNotEmpty();
        assertThat(actualResult).hasSize(3);
        assertThat(actualResult).isEqualTo(List.of(TestData.getTask1(), TestData.getTask2(), TestData.getTask3()));
    }

}