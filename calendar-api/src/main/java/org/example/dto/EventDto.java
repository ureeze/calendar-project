package org.example.dto;

import lombok.*;
import org.example.core.domain.ScheduleType;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@RequiredArgsConstructor
public class EventDto implements ScheduleDto {

    private final Long scheduleId;
    private final LocalDateTime startAt;
    private final LocalDateTime endAt;
    private final String title;
    private final String description;
    private final Long writerId;

    @Override
    public ScheduleType getScheduleType() {
        return ScheduleType.EVENT;
    }
}
