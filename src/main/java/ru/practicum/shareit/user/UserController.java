package ru.practicum.shareit.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exceptions.ConflictException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.messages.HandlerMessages;
import ru.practicum.shareit.messages.LogMessages;
import ru.practicum.shareit.user.model.User;


import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = "/users")
@Slf4j
public class UserController {
    public UserService userService;
    public UserDao userDao;

    @Autowired
    public UserController(UserService userService, UserDao userDao) {
        this.userService = userService;
        this.userDao = userDao;
    }

    @PostMapping
    @ResponseBody
    public User save(@Valid @RequestBody User user) {
        log.debug(String.valueOf(LogMessages.TRY_ADD), user);
        User user1 = userService.save(user);
        System.out.println(user1 + "  user1");
        return user1;
    }

    @GetMapping
    public List<User> getAll() {
        log.debug(String.valueOf(LogMessages.TRY_GET_ALL), "пользователей");
        return userService.getAll();
    }

    @GetMapping("/{userId}")
    public User getById(@PathVariable Long userId) {
        log.debug(String.valueOf(LogMessages.TRY_GET_OBJECT), userId);
        return userService.getById(userId);
    }


    @PutMapping
    public User update(@Valid @RequestBody User user) {
        log.debug(String.valueOf(LogMessages.TRY_UPDATE), user);
        return userService.update(user);
    }



    @DeleteMapping("/{userId}")
    public User removeById(@PathVariable Long userId) {
        log.debug(String.valueOf(LogMessages.TRY_REMOVE), userId);
        return userService.removeById(userId);
    }

    @PatchMapping(path = "/{id}", consumes = "application/json")
    public ResponseEntity<User> updateCustomer(@PathVariable Long id, @RequestBody JsonMergePatch patch) {
        try {
            User user = userDao.getById(id);
            User userPatched = applyPatchToUser(patch, user);
            userDao.getALL().stream().forEach(e-> System.out.println(e));
            System.out.println("++++++++++++++++++++++++++++++++++++++");
            System.out.println(user + "   user");
            System.out.println((patch + "   patch"));
            System.out.println(userPatched + "   userPatched");


            if (user.getName().equals(userPatched.getName()) && !user.getEmail().equals(userPatched.getEmail()))
            userDao.getALL().stream().forEach(e -> {if (userPatched.getEmail().equals(e.getEmail()))
                throw new ConflictException(String.valueOf(HandlerMessages.CONFLICT)); });

            userService.update(userPatched);
            return ResponseEntity.ok(userPatched);
        } catch (JsonPatchException | JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    private User applyPatchToUser(
            JsonMergePatch patch, User targetCustomer) throws JsonPatchException, JsonProcessingException {
        JsonNode patched = patch.apply(new ObjectMapper().convertValue(targetCustomer, JsonNode.class));
        return new ObjectMapper().treeToValue(patched, User.class);
    }

}
