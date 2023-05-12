package ru.practicum.shareit.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import com.sun.jdi.InternalException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import ru.practicum.shareit.abstracts.AbstractServiceImpl;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.messages.HandlerMessages;
import ru.practicum.shareit.messages.LogMessages;
import ru.practicum.shareit.user.dto.UserDtoRes;
import ru.practicum.shareit.user.model.User;

@Slf4j
@Service
public class UserService extends AbstractServiceImpl<User, UserRepository> {
    public UserService(UserRepository repository) {
        super(repository);
    }


    public UserDtoRes updateCustomer(Long id, JsonMergePatch patch) {
        try {
            User user = getById(id);
            User userPatched = applyPatchToUser(patch, user);
            log.debug(String.valueOf(LogMessages.TRY_PATCH), userPatched);

            update(userPatched);
            return new UserDtoRes(userPatched);
        } catch (JsonPatchException | JsonProcessingException e) {
            throw new InternalException(String.valueOf(HandlerMessages.SERVER_ERROR));
        } catch (NotFoundException e) {
            throw new NotFoundException(String.valueOf(HandlerMessages.NOT_FOUND));
        }
    }

    private User applyPatchToUser(
            JsonMergePatch patch, User targetCustomer) throws JsonPatchException, JsonProcessingException {
        JsonNode patched = patch.apply(new ObjectMapper().convertValue(targetCustomer, JsonNode.class));
        return new ObjectMapper().treeToValue(patched, User.class);
    }

}