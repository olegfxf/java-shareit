package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.abstracts.AbstractService;
import ru.practicum.shareit.abstracts.AbstractStorage;
import ru.practicum.shareit.user.model.User;

@Slf4j
@Service
public class UserService extends AbstractService<User, AbstractStorage<User>> {
    @Autowired
    public UserService(AbstractStorage<User> abstractStorage) {
        super(abstractStorage);
    }

}