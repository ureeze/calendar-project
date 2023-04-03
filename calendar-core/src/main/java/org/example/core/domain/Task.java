package org.example.core.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.core.domain.entity.Schedule;
import org.example.core.domain.entity.User;

@NoArgsConstructor
public class Task {
    private Schedule schedule;

    public Task(Schedule schedule) {
        this.schedule = schedule;
    }

    public String getTitle() {
        return schedule.getTitle();
    }

    public User getWriter() {
        return this.schedule.getWriter();
    }
}
