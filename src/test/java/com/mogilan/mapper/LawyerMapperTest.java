package com.mogilan.mapper;

import com.mogilan.TestData;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class LawyerMapperTest {

    LawyerMapper lawyerMapper = LawyerMapper.INSTANCE;

    @Test
    void toDtoSuccess() {
        var givenEntity = TestData.getLawyer1();
        var task1 = TestData.getTask1();
        var task2 = TestData.getTask2();
        var task3 = TestData.getTask3();
        givenEntity.setTasks(List.of(task1, task2, task3));

        var expectedResult = TestData.getLawyerDto1();
        var simpleTask1 = TestData.getSimpleTask1();
        var simpleTask2 = TestData.getSimpleTask2();
        var simpleTask3 = TestData.getSimpleTask3();
        expectedResult.setTasks(List.of(simpleTask1, simpleTask2, simpleTask3));

        var actualResult = lawyerMapper.toDto(givenEntity);

        assertThat(actualResult).isEqualTo(expectedResult);
        assertThat(actualResult.getTasks()).isNotEmpty();
        assertThat(actualResult.getTasks()).hasSize(3);
        assertThat(actualResult.getTasks()).isEqualTo(expectedResult.getTasks());
    }

    @Test
    void toEntitySuccess() {
        var givenDto = TestData.getLawyerDto1();
        var simpleTask1 = TestData.getSimpleTask1();
        var simpleTask2 = TestData.getSimpleTask2();
        var simpleTask3 = TestData.getSimpleTask3();
        givenDto.setTasks(List.of(simpleTask1, simpleTask2, simpleTask3));

        var expectedResult = TestData.getLawyer1();
        var task1 = TestData.getTask1();
        var task2 = TestData.getTask2();
        var task3 = TestData.getTask3();
        expectedResult.setTasks(List.of(task1, task2, task3));

        var actualResult = lawyerMapper.toEntity(givenDto);

        assertThat(actualResult).isEqualTo(expectedResult);
        assertThat(actualResult.getTasks()).isNotEmpty();
        assertThat(actualResult.getTasks()).hasSize(3);
        assertThat(actualResult.getTasks()).isEqualTo(expectedResult.getTasks());
    }

    @Test
    void toDtoListSuccess() {
        var givenList = List.of(TestData.getLawyer1(), TestData.getLawyer2(), TestData.getLawyer3());
        var expectedResult = List.of(TestData.getLawyerDto1(), TestData.getLawyerDto2(), TestData.getLawyerDto3());

        var actualResult = lawyerMapper.toDtoList(givenList);

        assertThat(actualResult).isNotEmpty();
        assertThat(actualResult).hasSize(3);
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    void toEntityListSuccess() {
        var givenList = List.of(TestData.getLawyerDto1(), TestData.getLawyerDto2(), TestData.getLawyerDto3());
        var expectedResult = List.of(TestData.getLawyer1(), TestData.getLawyer2(), TestData.getLawyer3());

        var actualResult = lawyerMapper.toEntityList(givenList);

        assertThat(actualResult).isNotEmpty();
        assertThat(actualResult).hasSize(3);
        assertThat(actualResult).isEqualTo(expectedResult);
    }
}