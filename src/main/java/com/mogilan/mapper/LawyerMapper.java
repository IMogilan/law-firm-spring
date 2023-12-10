package com.mogilan.mapper;

import com.mogilan.dto.LawyerDto;
import com.mogilan.model.Lawyer;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring",
        uses = SimpleTaskMapper.class)
public interface LawyerMapper {

    LawyerDto toDto(Lawyer entity);

    Lawyer toEntity(LawyerDto dto);

    List<LawyerDto> toDtoList(List<Lawyer> entityList);

    List<Lawyer> toEntityList(List<LawyerDto> dtoList);
}
