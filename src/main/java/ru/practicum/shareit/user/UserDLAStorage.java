package ru.practicum.shareit.user;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.abstracts.AbstractDLAStorage;
import ru.practicum.shareit.user.model.User;

@Component
public class UserDLAStorage extends AbstractDLAStorage<User> {

//    @Override
//    public User save(Object obj) {
//        getALL().stream().forEach(e -> {
//            if (((User) obj).getEmail().equals(e.getEmail()))
//                throw new ConflictException(String.valueOf(HandlerMessages.CONFLICT));
//        });
//        ((User) obj).setId(id++);
//        mem.put(((User) obj).getId(), (User) obj);
//        return (User) obj;
//    }

}
