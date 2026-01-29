package com.ergun.connectsphere.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CallEventDto {

    public enum CallType {
        OFFER,
        ANSWER,
        ICE_CANDIDATE,
        HANGUP,
        REJECT
    }

    private CallType type;

    private Long fromUserId;
    private Long toUserId;

    /**
     * SDP veya ICE Candidate bilgisi
     * (JSON string veya plain text olabilir)
     */
    private String data;
}