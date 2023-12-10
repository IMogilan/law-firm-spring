package com.mogilan.mapper;

import com.mogilan.dto.ClientDto;
import com.mogilan.model.Client;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring",
        uses = SimpleTaskMapper.class)
public interface ClientMapper {

    ClientDto toDto(Client entity);

    Client toEntity(ClientDto dto);

    List<ClientDto> toDtoList(List<Client> entityList);

    List<Client> toEntityList(List<ClientDto> dtoList);
}
