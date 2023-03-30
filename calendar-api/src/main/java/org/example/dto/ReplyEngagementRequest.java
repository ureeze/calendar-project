package org.example.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.core.domain.RequestReplyType;

@RequiredArgsConstructor
@Getter
public class ReplyEngagementRequest {
    private RequestReplyType type;
}
