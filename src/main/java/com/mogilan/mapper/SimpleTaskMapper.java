package com.mogilan.mapper;

import com.mogilan.dto.SimpleTaskDto;
import com.mogilan.model.Task;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface SimpleTaskMapper {

    SimpleTaskMapper INSTANCE = Mappers.getMapper(SimpleTaskMapper.class);

    SimpleTaskDto toSimpleTaskDto(Task task);

    Task toTask(SimpleTaskDto simpleTaskDto);

    List<SimpleTaskDto> toSimpleTaskDtoList(List<Task> taskDtoList);

    List<Task> toTaskList(List<SimpleTaskDto> simpleTaskDtoList);
}
