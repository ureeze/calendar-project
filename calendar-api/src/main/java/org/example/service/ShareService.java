package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.core.domain.RequestReplyType;
import org.example.core.domain.RequestStatus;
import org.example.core.domain.entity.Share;
import org.example.core.domain.entity.User;
import org.example.core.domain.entity.repository.ShareRepository;
import org.example.core.exception.CalendarException;
import org.example.core.exception.ErrorCode;
import org.example.core.service.UserService;
import org.example.dto.CreateShareRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ShareService {
    private final UserService userService;
    private final ShareRepository shareRepository;
    private final EmailService emailService;

    @Transactional
    public Share createShare(Long fromUserId, Long toUserId, Share.Direction direction) {
        final User toUser = userService.findByUserId(toUserId);
        final User fromUser = userService.findByUserId(fromUserId);
        emailService.sendShareRequestMail(toUser.getEmail(), fromUser.getEmail(), direction);
        return shareRepository.save(Share.builder()
                .fromUserId(fromUserId)
                .toUserId(toUserId)
                .direction(direction)
                .requestStatus(RequestStatus.REQUESTED)
                .build());
    }

    @Transactional
    public void replyToShareRequest(Long shareId, Long toUserId, RequestReplyType type) {
        shareRepository.findById(shareId)
                .filter(s -> s.getToUserId().equals(toUserId))
                .filter(s -> s.getRequestStatus() == RequestStatus.REQUESTED)
                .map(share -> share.reply(type))
                .orElseThrow(() -> new CalendarException(ErrorCode.BAD_REQUEST));
    }
}
