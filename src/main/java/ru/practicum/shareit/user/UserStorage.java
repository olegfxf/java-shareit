package ru.practicum.shareit.user;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserStorage {
    User save(User user);

    List<User> getALL();

    User getById(Long id);

    User update(User user);

    User removeById(Long id);

    void removeALL();
}
