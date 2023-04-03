package org.example.core.domain;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.example.core.domain.entity.Schedule;
import org.example.core.domain.entity.User;
import org.example.core.util.Period;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public class Event {
    private final Schedule schedule;


    public boolean isOverlapped(LocalDateTime startAt, LocalDateTime endAt) {
        return schedule.getStartAt().isBefore(endAt)
                && startAt.isBefore(schedule.getEndAt());
    }

    public Long getId() {
        return this.schedule.getId();
    }

    public LocalDateTime getStartAt() {
        return schedule.getStartAt();
    }

    public LocalDateTime getEndAt() {
        return schedule.getEndAt();
    }

    public User getWriter() {
        return this.schedule.getWriter();
    }


    public String getTitle() {
        return this.schedule.getTitle();
    }

    public Period getPeriod() {
        return Period.of(this.schedule.getStartAt(), this.schedule.getEndAt());
    }
}
