package com.teya.tiny_bank.service;

import com.teya.tiny_bank.dao.UserDao;
import com.teya.tiny_bank.dto.UserDto;
import com.teya.tiny_bank.mapper.UserMapper;
import com.teya.tiny_bank.model.User;
import com.teya.tiny_bank.model.enums.AccountStatus;
import com.teya.tiny_bank.model.enums.UserStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    private final UserMapper mapper;

    private final AccountService accountService;

    @Override
    //@Transactional
    public UserDto create(UserDto userDto) {
        User user = mapper.mapToDao(userDto);
        UUID id = UUID.randomUUID();
        user.setId(id);
        user.setStatus(UserStatus.ACTIVE);
        user.setDateCreated(LocalDateTime.now());
        user.setDateUpdated(LocalDateTime.now());
        userDao.save(user);
        accountService.createAccountForUser(id);
        return mapper.mapToDto(user);
    }

    @Override
    //@Transactional
    public void deactivate(UUID id) {
        User user = userDao.get(id);
        user.setStatus(UserStatus.DEACTIVATED);
        user.setDateUpdated(LocalDateTime.now());
        userDao.save(user);
        List<UUID> userAccountsIds = accountService.getAccountsIdsByUserId(id);
        for (UUID accountsId : userAccountsIds) {
            accountService.changeStatus(accountsId, AccountStatus.CLOSED);
        }
    }

    @Override
    public UserDto getById(UUID id) {
        return mapper.mapToDto(userDao.get(id));
    }

    @Override
    public UUID createAccountForUser(UUID userId) {
        User user = userDao.get(userId);
        if (Objects.nonNull(user)) {
            return accountService.createAccountForUser(userId);
        }
        throw new RuntimeException("User not found");
    }
}