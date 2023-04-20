package ru.practicum.shareit.abstracts;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.messages.ExceptionMessages;
import ru.practicum.shareit.messages.LogMessages;

import java.util.List;

public abstract class AbstractService<E, S extends AbstractStorage<E>> {
    protected final S abstractStorage;
    protected Logger log = LoggerFactory.getLogger(this.getClass());

    public AbstractService(S abstractStorage) {
        this.abstractStorage = abstractStorage;
    }

    public E save(E obj) {
        E obj1 = abstractStorage.save(obj);
        log.debug(String.valueOf(LogMessages.ADD), obj1);
        return obj1;
    }

    public List<E> getAll() {
        log.debug(String.valueOf(LogMessages.GET_ALL_USERS), abstractStorage.getALL());
        return abstractStorage.getALL();
    }

    public List<E> getAll(Long id) {
        log.debug(String.valueOf(LogMessages.GET_ALL_USERS), abstractStorage.getALL());
        return abstractStorage.getAll(id);
    }

    public E getById(Long objId) {
        if (abstractStorage.getById(objId) == null)
            throw new NotFoundException(ExceptionMessages.NOT_OBJECT);
        log.debug(String.valueOf(LogMessages.GET), abstractStorage.getById(objId));
        return abstractStorage.getById(objId);
    }

    public E update(E obj) {
        E obj1 = abstractStorage.update(obj);
        log.debug(String.valueOf(LogMessages.UPDATE), obj1);
        return obj1;
    }

    public E removeById(Long objId) {
        E obj = abstractStorage.removeById(objId);
        log.debug(String.valueOf(LogMessages.REMOVE), obj);
        return obj;
    }

    public List<E> search(String text) {
        return null;
    }

}
