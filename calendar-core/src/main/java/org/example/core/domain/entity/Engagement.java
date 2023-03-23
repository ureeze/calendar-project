package org.example.core.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.core.domain.RequestStatus;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Table(name = "engagements")
@Entity
public class Engagement extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    @ManyToOne
    @JoinColumn(name = "attendee_id")
    private User attendee;
    private RequestStatus requestStatus;


}
