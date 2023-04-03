package org.example.dto;

import lombok.Data;
import org.example.core.domain.entity.Share;

@Data
public class CreateShareRequest {

    private final Long toUserId;
    private final Share.Direction direction;
}
