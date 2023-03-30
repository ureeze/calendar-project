package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.core.domain.RequestReplyType;
import org.example.core.domain.RequestStatus;
import org.example.core.domain.entity.Engagement;
import org.example.core.domain.entity.repository.EngagementRepository;
import org.example.core.exception.CalendarException;
import org.example.core.exception.ErrorCode;
import org.example.dto.AuthUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EngagementService {

    private final EngagementRepository engagementRepository;

    @Transactional
    public RequestStatus update(AuthUser authUser, Long engagementId, RequestReplyType type) {
        // engagement 조회
        // 참석자가 authUser 와 같은 지 비교
        // requested 상태인지 체크
        // update
        engagementRepository.findById(engagementId)
                .filter(engagement -> engagement.getRequestStatus() == RequestStatus.REQUESTED)
                .filter(engagement -> engagement.getAttendee().getId() == authUser.getId())
                .map(engagement -> engagement.updateStatus(type))
                .orElseThrow(() -> new CalendarException(ErrorCode.BAD_REQUEST))
                .getRequestStatus();
        return RequestStatus.ACCEPTED;
    }
}
