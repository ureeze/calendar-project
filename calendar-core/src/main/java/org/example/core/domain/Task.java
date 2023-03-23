package org.example.core.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.core.domain.entity.Schedule;

@NoArgsConstructor
@Getter
public class Task {
    private Schedule schedule;

    public Task(Schedule schedule) {
        this.schedule = schedule;
    }

    public String getTitle() {
        return schedule.getTitle();
    }
}
