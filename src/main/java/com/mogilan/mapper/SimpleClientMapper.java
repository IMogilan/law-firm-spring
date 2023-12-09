package com.mogilan.mapper;

import com.mogilan.dto.SimpleClientDto;
import com.mogilan.model.Client;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface SimpleClientMapper {

    SimpleClientMapper INSTANCE = Mappers.getMapper(SimpleClientMapper.class);

    SimpleClientDto toSimpleClientDto(Client client);

    Client toClient(SimpleClientDto simpleClientDto);

    List<SimpleClientDto> toSimpleClientDtoList(List<Client> clientList);

    List<Client> toClientList(List<SimpleClientDto> simpleClientDtoList);
}
