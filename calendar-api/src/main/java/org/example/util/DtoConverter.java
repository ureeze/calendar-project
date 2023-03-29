package org.example.util;

import org.example.core.domain.entity.Schedule;
import org.example.dto.EventDto;
import org.example.dto.NotificationDto;
import org.example.dto.ScheduleDto;
import org.example.dto.TaskDto;

public abstract class DtoConverter {
    public static ScheduleDto fromSchedule(Schedule schedule) {
        switch (schedule.getScheduleType()) {
            case TASK:
                return TaskDto.builder()
                        .title(schedule.getTitle())
                        .scheduleId(schedule.getId())
                        .taskAt(schedule.getStartAt())
                        .description(schedule.getDescription())
                        .writerId(schedule.getWriter().getId())
                        .build();
            case EVENT:
                return EventDto.builder()
                        .title(schedule.getTitle())
                        .scheduleId(schedule.getId())
                        .description(schedule.getDescription())
                        .writerId(schedule.getWriter().getId())
                        .startAt(schedule.getStartAt())
                        .endAt(schedule.getEndAt())
                        .build();
            case NOTIFICATION:
                return NotificationDto.builder()
                        .title(schedule.getTitle())
                        .notifyAt(schedule.getStartAt())
                        .scheduleId(schedule.getId())
                        .writerId(schedule.getWriter().getId())
                        .build();
            default:
                return null;
        }
    }
}
