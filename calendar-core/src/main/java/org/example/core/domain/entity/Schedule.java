package org.example.core.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.core.domain.Event;
import org.example.core.domain.Notification;
import org.example.core.domain.ScheduleType;
import org.example.core.domain.Task;
import org.example.core.util.Period;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "schedules")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Schedule extends BaseEntity {


    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private String title;
    private String description;

    @ManyToOne
    @JoinColumn(name = "writer_id")
    private User writer;

    @Enumerated(EnumType.STRING)
    private ScheduleType scheduleType;



    public static Schedule event(String title, String description, LocalDateTime startAt, LocalDateTime endAt, User writer) {
        return Schedule.builder()
                .title(title)
                .description(description)
                .startAt(startAt)
                .endAt(endAt)
                .writer(writer)
                .scheduleType(ScheduleType.EVENT)
                .build();
    }

    public static Schedule task(String title, String description, LocalDateTime notifyAt, User writer) {
        return Schedule.builder()
                .title(title)
                .description(description)
                .startAt(notifyAt)
                .writer(writer)
                .scheduleType(ScheduleType.TASK)
                .build();
    }

    public static Schedule notification(String title, LocalDateTime notifyAt, User writer) {
        return Schedule.builder()
                .title(title)
                .startAt(notifyAt)
                .writer(writer)
                .scheduleType(ScheduleType.NOTIFICATION)
                .build();
    }

    public Task toTask() {
        return new Task(this);
    }

    public Event toEvent() {
        return new Event(this);
    }

    public Notification toNotification() {
        return new Notification(this);
    }

    public boolean isOverlapped(LocalDate date) {
        return Period.of(getStartAt(), getEndAt()).isOverlapped(date);
    }

    public boolean isOverlapped(Period period) {
        return Period.of(getStartAt(), getEndAt()).isOverlapped(period);
    }

    @Override
    public String toString() {
        return "Schedule{" +
                "id=" + super.getId().toString() +
                ", startAt=" + startAt +
                ", endAt=" + endAt +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", writer=" + writer +
                ", scheduleType=" + scheduleType +
                '}';
    }
}
