package org.example.core.domain.entity.repository;

import org.example.core.domain.RequestStatus;
import org.example.core.domain.entity.Share;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ShareRepository extends JpaRepository<Share, Long> {
    @Query("select s from Share s where (s.fromUserId = ?1 or s.toUserId=?1) and s.requestStatus=?2 and s.direction = ?3")
    List<Share> findAllByBiDirection(Long id, RequestStatus requestStatus, Share.Direction direction);

    List<Share> findAllByToUserIdAndRequestStatusAndDirection(Long fromUserId, RequestStatus requestStatus, Share.Direction direction);
}