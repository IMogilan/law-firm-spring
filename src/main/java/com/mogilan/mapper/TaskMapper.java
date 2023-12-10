package com.mogilan.mapper;

import com.mogilan.dto.TaskDto;
import com.mogilan.model.Task;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring",
        uses = {SimpleClientMapper.class, SimpleLawyerMapper.class})
public interface TaskMapper {

    TaskDto toDto(Task entity);

    Task toEntity(TaskDto dto);

    List<TaskDto> toDtoList(List<Task> entityList);

    List<Task> toEntityList(List<TaskDto> dtoList);
}
