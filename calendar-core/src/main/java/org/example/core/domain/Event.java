package org.example.core.domain;


import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.core.domain.entity.Schedule;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class Event {
    private Schedule schedule;

    public Event(Schedule schedule) {
        this.schedule = schedule;
    }

    public boolean isOverlapped(LocalDateTime startAt, LocalDateTime endAt) {
        return schedule.getStartAt().isBefore(endAt)
                && startAt.isBefore(schedule.getEndAt());
    }
}
