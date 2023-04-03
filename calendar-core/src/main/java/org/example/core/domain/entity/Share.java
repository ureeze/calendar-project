package org.example.core.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.core.domain.RequestReplyType;
import org.example.core.domain.RequestStatus;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import static org.example.core.domain.RequestReplyType.ACCEPT;

@Builder
@Getter
@Entity
@Table(name = "shares")
@NoArgsConstructor
@AllArgsConstructor
public class Share extends BaseEntity {

    private Long fromUserId;
    private Long toUserId;

    @Enumerated(value = EnumType.STRING)
    private RequestStatus requestStatus;

    @Enumerated(value = EnumType.STRING)
    private Direction direction; // From 이 To 에게 요청. 단방향인 경우는 To 의 캘린더가 From 에 보인다.

    public Share reply(RequestReplyType type) {
        switch (type) {
            case ACCEPT:
                this.requestStatus = RequestStatus.ACCEPTED;
                break;
            case REJECT:
                this.requestStatus = RequestStatus.REJECTED;
                break;
        }
        return this;
    }

    public enum Direction {
        BI_DIRECTION, UNI_DIRECTION
    }

}
