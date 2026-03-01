package com.example.bankcards.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
public class CardDto {
    private Long id;
    private String cardNumber;
    private BigDecimal balance;
    private Date expireDate;
    private String status;
    private Long ownerId;
    private String ownerName;
}