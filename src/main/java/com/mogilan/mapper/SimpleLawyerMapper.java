package com.mogilan.mapper;


import com.mogilan.dto.SimpleLawyerDto;
import com.mogilan.model.Lawyer;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
@Mapper
public interface SimpleLawyerMapper {

    SimpleLawyerMapper INSTANCE = Mappers.getMapper(SimpleLawyerMapper.class);

    SimpleLawyerDto toSimpleLawyerDto(Lawyer lawyer);

    Lawyer toLawyer(SimpleLawyerDto simpleLawyerDto);

    List<SimpleLawyerDto> toSimpleLawyerDtoList(List<Lawyer> lawyerList);

    List<Lawyer> toLawyerList(List<SimpleLawyerDto> simpleLawyerDtoList);

}
