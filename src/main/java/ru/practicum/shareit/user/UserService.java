package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.abstracts.AbstractServiceImpl;
import ru.practicum.shareit.user.model.User;

@Slf4j
@Service
public class UserService extends AbstractServiceImpl<User, UserRepository> {
    public UserService(UserRepository repository) {
        super(repository);
    }

}