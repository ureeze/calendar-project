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
import org.example.dto.AuthUser;
import org.example.dto.CreateShareRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
                .filter(share -> share.getToUserId().equals(toUserId))
                .filter(share -> share.getRequestStatus() == RequestStatus.REQUESTED)
                .map(share -> share.reply(type))
                .orElseThrow(() -> new CalendarException(ErrorCode.BAD_REQUEST));
    }

    /**
     * 공유 대상 조회
     * <p>
     * 자신과 양방향 공유관계인 상대방 (자신이 to, from 둘다 가능)
     * 내가 공유관계의 수신자(toUser) 인 경우 & 단방향
     *
     * @param authUser
     * @return
     */
    @Transactional
    public List<Long> findSharedUserIdsByUser(AuthUser authUser) {

        return Stream.concat(
                        shareRepository.findAllByBiDirection(
                                        authUser.getId(),
                                        RequestStatus.REQUESTED,
                                        Share.Direction.BI_DIRECTION)
                                .stream()
                                .map(share -> share.getToUserId().equals(authUser.getId()) ? authUser.getId() : share.getToUserId()),
                        shareRepository.findAllByToUserIdAndRequestStatusAndDirection(
                                        authUser.getId(),
                                        RequestStatus.REQUESTED,
                                        Share.Direction.UNI_DIRECTION)
                                .stream()
                                .map(share -> share.getFromUserId()))
                .collect(Collectors.toList());


    }
}
