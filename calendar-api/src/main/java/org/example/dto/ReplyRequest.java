package org.example.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.core.domain.RequestReplyType;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class ReplyRequest {

    @NotNull
    private RequestReplyType type;
}
