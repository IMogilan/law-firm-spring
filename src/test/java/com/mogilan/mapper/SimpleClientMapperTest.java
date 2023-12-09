package com.mogilan.mapper;

import com.mogilan.TestData;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SimpleClientMapperTest {

    SimpleClientMapper simpleClientMapper = SimpleClientMapper.INSTANCE;

    @Test
    void toSimpleClientDtoSuccess() {
        var client1 = TestData.getClient1();
        var client2 = TestData.getClient2();
        var client3 = TestData.getClient3();

        var actualResult1 = simpleClientMapper.toSimpleClientDto(client1);
        var actualResult2 = simpleClientMapper.toSimpleClientDto(client2);
        var actualResult3 = simpleClientMapper.toSimpleClientDto(client3);

        assertThat(actualResult1).isEqualTo(TestData.getSimpleClient1());
        assertThat(actualResult2).isEqualTo(TestData.getSimpleClient2());
        assertThat(actualResult3).isEqualTo(TestData.getSimpleClient3());
    }

    @Test
    void toClientSuccess() {
        var simpleClient1 = TestData.getSimpleClient1();
        var simpleClient2 = TestData.getSimpleClient2();
        var simpleClient3 = TestData.getSimpleClient3();

        var actualResult1 = simpleClientMapper.toClient(simpleClient1);
        var actualResult2 = simpleClientMapper.toClient(simpleClient2);
        var actualResult3 = simpleClientMapper.toClient(simpleClient3);

        assertThat(actualResult1).isEqualTo(TestData.getClient1());
        assertThat(actualResult2).isEqualTo(TestData.getClient2());
        assertThat(actualResult3).isEqualTo(TestData.getClient3());
    }

    @Test
    void toSimpleClientDtoListSuccess() {
        var client1 = TestData.getClient1();
        var client2 = TestData.getClient2();
        var client3 = TestData.getClient3();

        var actualResult = simpleClientMapper.toSimpleClientDtoList(List.of(client1, client2, client3));

        assertThat(actualResult).isNotEmpty();
        assertThat(actualResult).hasSize(3);
        assertThat(actualResult).isEqualTo(List.of(TestData.getSimpleClient1(), TestData.getSimpleClient2(), TestData.getSimpleClient3()));
    }

    @Test
    void toClientListSuccess() {
        var simpleClient1 = TestData.getSimpleClient1();
        var simpleClient2 = TestData.getSimpleClient2();
        var simpleClient3 = TestData.getSimpleClient3();

        var actualResult = simpleClientMapper.toClientList(List.of(simpleClient1, simpleClient2, simpleClient3));

        assertThat(actualResult).isNotEmpty();
        assertThat(actualResult).hasSize(3);
        assertThat(actualResult).isEqualTo(List.of(TestData.getClient1(), TestData.getClient2(), TestData.getClient3()));
    }
}