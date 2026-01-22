package com.ergun.connectsphere.dto;


import lombok.Data;

@Data
public class ChatGroupCreateRequestDto {

    private String name;
    private String description;
    private Long creatorId;
}