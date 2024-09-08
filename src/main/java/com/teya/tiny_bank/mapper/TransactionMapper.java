package com.teya.tiny_bank.mapper;

import com.teya.tiny_bank.dto.TransactionDto;
import com.teya.tiny_bank.model.Transaction;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    TransactionDto mapToDto(Transaction source);

    Transaction mapToDao(TransactionDto source);
}