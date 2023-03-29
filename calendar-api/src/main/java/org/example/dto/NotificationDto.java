package org.example.dto;

import lombok.*;
import org.example.core.domain.ScheduleType;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@RequiredArgsConstructor
public class NotificationDto implements ScheduleDto {

    private final Long scheduleId;
    private final LocalDateTime notifyAt;
    private final String title;
    private final Long writerId;

    @Override
    public ScheduleType getScheduleType() {
        return ScheduleType.NOTIFICATION;
    }
}
