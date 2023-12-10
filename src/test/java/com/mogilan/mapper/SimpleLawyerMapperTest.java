package com.mogilan.mapper;

import com.mogilan.TestData;
import com.mogilan.config.WebConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = WebConfig.class)
@WebAppConfiguration()
class SimpleLawyerMapperTest {

    @Autowired
    SimpleLawyerMapper simpleLawyerMapper;

    @Test
    void toSimpleLawyerDtoSuccess() {
        var lawyer1 = TestData.getLawyer1();
        var lawyer2 = TestData.getLawyer2();
        var lawyer3 = TestData.getLawyer3();

        var actualResult1 = simpleLawyerMapper.toSimpleLawyerDto(lawyer1);
        var actualResult2 = simpleLawyerMapper.toSimpleLawyerDto(lawyer2);
        var actualResult3 = simpleLawyerMapper.toSimpleLawyerDto(lawyer3);

        assertThat(actualResult1).isEqualTo(TestData.getSimpleLawyer1());
        assertThat(actualResult2).isEqualTo(TestData.getSimpleLawyer2());
        assertThat(actualResult3).isEqualTo(TestData.getSimpleLawyer3());
    }

    @Test
    void toLawyerSuccess() {
        var simpleLawyer1 = TestData.getSimpleLawyer1();
        var simpleLawyer2 = TestData.getSimpleLawyer2();
        var simpleLawyer3 = TestData.getSimpleLawyer3();

        var actualResult1 = simpleLawyerMapper.toLawyer(simpleLawyer1);
        var actualResult2 = simpleLawyerMapper.toLawyer(simpleLawyer2);
        var actualResult3 = simpleLawyerMapper.toLawyer(simpleLawyer3);

        assertThat(actualResult1).isEqualTo(TestData.getLawyer1());
        assertThat(actualResult2).isEqualTo(TestData.getLawyer2());
        assertThat(actualResult3).isEqualTo(TestData.getLawyer3());
    }

    @Test
    void toSimpleLawyerDtoListSuccess() {
        var lawyer1 = TestData.getLawyer1();
        var lawyer2 = TestData.getLawyer2();
        var lawyer3 = TestData.getLawyer3();

        var actualResult = simpleLawyerMapper.toSimpleLawyerDtoList(List.of(lawyer1, lawyer2, lawyer3));

        assertThat(actualResult).isNotEmpty();
        assertThat(actualResult).hasSize(3);
        assertThat(actualResult).isEqualTo(List.of(TestData.getSimpleLawyer1(), TestData.getSimpleLawyer2(), TestData.getSimpleLawyer3()));
    }

    @Test
    void toLawyerListSuccess() {
        var simpleLawyer1 = TestData.getSimpleLawyer1();
        var simpleLawyer2 = TestData.getSimpleLawyer2();
        var simpleLawyer3 = TestData.getSimpleLawyer3();

        var actualResult = simpleLawyerMapper.toLawyerList(List.of(simpleLawyer1, simpleLawyer2, simpleLawyer3));

        assertThat(actualResult).isNotEmpty();
        assertThat(actualResult).hasSize(3);
        assertThat(actualResult).isEqualTo(List.of(TestData.getLawyer1(), TestData.getLawyer2(), TestData.getLawyer3()));
    }
}