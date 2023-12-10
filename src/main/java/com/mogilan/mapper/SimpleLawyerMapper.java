package com.mogilan.mapper;


import com.mogilan.dto.SimpleLawyerDto;
import com.mogilan.model.Lawyer;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
@Mapper(componentModel = "spring")
public interface SimpleLawyerMapper {

    SimpleLawyerDto toSimpleLawyerDto(Lawyer lawyer);

    Lawyer toLawyer(SimpleLawyerDto simpleLawyerDto);

    List<SimpleLawyerDto> toSimpleLawyerDtoList(List<Lawyer> lawyerList);

    List<Lawyer> toLawyerList(List<SimpleLawyerDto> simpleLawyerDtoList);

}
