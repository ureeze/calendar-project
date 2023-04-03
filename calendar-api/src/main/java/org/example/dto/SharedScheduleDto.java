package org.example.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SharedScheduleDto {

    private final Long userId;
    private final String name;
    private final boolean me;
    private final List<ScheduleDto> schedules;
}
