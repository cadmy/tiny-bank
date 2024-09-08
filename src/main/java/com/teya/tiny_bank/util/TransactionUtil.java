package com.teya.tiny_bank.util;

import com.teya.tiny_bank.dto.TransactionDto;

import java.util.Objects;

public class TransactionUtil {

    public static boolean isValid(TransactionDto transactionDto) {
        if (transactionDto.getAmount() < 0L) {
            return false;
        }
        return Objects.nonNull(transactionDto.getDeposit()) || Objects.nonNull(transactionDto.getCredit());
    }
}