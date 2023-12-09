package com.mogilan.mapper;

import com.mogilan.dto.LawyerDto;
import com.mogilan.model.Lawyer;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(uses = SimpleTaskMapper.class)
public interface LawyerMapper {

    LawyerMapper INSTANCE = Mappers.getMapper(LawyerMapper.class);

    LawyerDto toDto(Lawyer entity);

    Lawyer toEntity(LawyerDto dto);

    List<LawyerDto> toDtoList(List<Lawyer> entityList);

    List<Lawyer> toEntityList(List<LawyerDto> dtoList);
}
