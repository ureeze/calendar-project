package org.example.core.domain;


import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.core.domain.entity.Schedule;
import org.example.core.util.Period;

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

    public String getTitle() {
        return this.schedule.getTitle();
    }

    public Period getPeriod() {
        return Period.of(this.schedule.getStartAt(), this.schedule.getEndAt());
    }
}
