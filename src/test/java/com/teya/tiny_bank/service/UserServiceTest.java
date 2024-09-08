package com.teya.tiny_bank.service;

import com.teya.tiny_bank.dao.UserDao;
import com.teya.tiny_bank.dto.UserDto;
import com.teya.tiny_bank.mapper.UserMapper;
import com.teya.tiny_bank.model.Account;
import com.teya.tiny_bank.model.User;
import com.teya.tiny_bank.model.enums.AccountStatus;
import com.teya.tiny_bank.model.enums.UserStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserDao userDao;

    @MockBean
    private AccountService accountService;

    @Autowired
    private UserMapper userMapper;

    @Test
    public void create_Success() {
        final String name = "Harry";
        final String surname = "Potter";
        UserDto userDto = new UserDto();
        userDto.setName(name);
        userDto.setSurname(surname);

        User user = new User();
        user.setName(name);
        user.setSurname(surname);

        when(userDao.save(any(User.class))).thenReturn(user);

        UserDto result = userService.create(userDto);

        assertNotNull(result);
        assertEquals(name, result.getName());
        assertEquals(surname, result.getSurname());
        assertEquals(UserStatus.ACTIVE, result.getStatus());
        assertNotNull(result.getDateCreated());
        assertNotNull(result.getDateUpdated());

        verify(userDao).save(any(User.class));
        verify(accountService).createAccountForUser(any(UUID.class));
    }

    @Test
    public void deactivate_Success() {
        final String name = "Frodo";
        final String surname = "Beggins";
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);
        user.setName(name);
        user.setSurname(surname);

        when(userDao.get(userId)).thenReturn(user);
        when(userDao.save(any(User.class))).thenReturn(user);
        UUID accountId = UUID.randomUUID();
        when(accountService.getAccountsIdsByUserId(userId)).thenReturn(List.of(accountId));

        userService.deactivate(userId);

        verify(userDao).save(any(User.class));
        verify(accountService).getAccountsIdsByUserId(userId);
        verify(accountService).changeStatus(accountId, AccountStatus.CLOSED);
    }

    @Test
    public void getById_Success() {
        final String name = "Sherlock";
        final String surname = "Holmes";
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);
        user.setName(name);
        user.setStatus(UserStatus.ACTIVE);
        user.setSurname(surname);
        LocalDateTime now = LocalDateTime.now();
        user.setDateCreated(now);
        user.setDateUpdated(now);

        when(userDao.get(userId)).thenReturn(user);

        UserDto result = userService.getById(userId);

        assertEquals(name, result.getName());
        assertEquals(surname, result.getSurname());
        assertEquals(UserStatus.ACTIVE, result.getStatus());
        assertEquals(now, result.getDateCreated());
        assertEquals(now, result.getDateUpdated());

        verify(userDao).get(userId);
    }

    @Test
    public void createAccountForUser_Success() {
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);

        when(userDao.get(userId)).thenReturn(user);
        UUID accountId = UUID.randomUUID();
        when(accountService.createAccountForUser(userId)).thenReturn(accountId);

        assertEquals(accountId, userService.createAccountForUser(userId));

        verify(userDao).get(userId);
        verify(accountService).createAccountForUser(userId);
    }

    @Test
    public void createAccountForUser_UserNotFound() {
        UUID userId = UUID.randomUUID();

        when(userDao.get(userId)).thenReturn(null);

        assertThrows(RuntimeException.class, () -> {
            userService.createAccountForUser(userId);
        });

        verify(userDao).get(userId);
        verify(accountService, never()).createAccountForUser(userId);
    }
}