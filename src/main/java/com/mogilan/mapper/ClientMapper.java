package com.mogilan.mapper;

import com.mogilan.dto.ClientDto;
import com.mogilan.model.Client;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(uses = SimpleTaskMapper.class)
public interface ClientMapper {

    ClientMapper INSTANCE = Mappers.getMapper(ClientMapper.class);

    ClientDto toDto(Client entity);

    Client toEntity(ClientDto dto);

    List<ClientDto> toDtoList(List<Client> entityList);

    List<Client> toEntityList(List<ClientDto> dtoList);
}
