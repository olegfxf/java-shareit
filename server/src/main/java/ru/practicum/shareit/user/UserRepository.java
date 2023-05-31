package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.abstracts.CommonRepository;
import ru.practicum.shareit.user.model.User;

@Repository
public interface UserRepository extends CommonRepository<User> {
    boolean existsById(Long id);

    boolean existsUserByEmail(String email);
}
