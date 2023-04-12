package ru.practicum.shareit.user;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.ConflictException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.messages.ExceptionMessages;
import ru.practicum.shareit.messages.HandlerMessages;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@Component
public class UserDao {
    static Map<Long, User> mem = new HashMap<>();
    static Long id = 1L;

    public User save(User user) {
        getALL().stream().forEach(e -> {if (user.getEmail().equals(e.getEmail()))
                          throw new ConflictException(String.valueOf(HandlerMessages.CONFLICT)); });
        user.setId(id++);
        mem.put(user.getId(), user);
        return user;
    }

    public List<User> getALL() {
        return mem.values().stream().collect(toList());
    }

    public User getById(Long id) {
        //mem.values().stream().forEach(e -> System.out.println(e + "  UserDao.getById"));
        return mem.get(id);
    }

    public User update(User user) {
        Long id = user.getId();
        if (mem.get(id) == null)
            throw new NotFoundException(String.valueOf(ExceptionMessages.NOT_FOUND_ID));

//        getALL().stream().forEach(e -> {
//            if (user.getEmail().equals(e.getEmail()))
//                throw new ConflictException(String.valueOf(HandlerMessages.CONFLICT));
//        });

        mem.put(id, user);
//        System.out.println("!!!!!!!!!");
//        mem.values().stream().forEach(e -> System.out.println(e + "  UserDao"));
        return user;
    }

    public User removeById(Long userId) {
        return mem.remove(userId);
    }


}
