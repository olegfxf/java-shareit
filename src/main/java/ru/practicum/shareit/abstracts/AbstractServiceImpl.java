package ru.practicum.shareit.abstracts;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.ConflictException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.messages.ExceptionMessages;
import ru.practicum.shareit.messages.HandlerMessages;
import ru.practicum.shareit.messages.LogMessages;

import java.util.List;

@Service
public abstract class AbstractServiceImpl<E extends AbstractModel, R extends CommonRepository<E>>
        implements AbstractService<E> {

    private final R repository;
    protected Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public AbstractServiceImpl(R repository) {
        this.repository = repository;
    }

    //@Transactional
    public E save(E obj) {
        E obj1;
        try {
            obj1 = repository.save(obj);
        } catch (Exception e) {
            throw new ConflictException(String.valueOf(HandlerMessages.CONFLICT));
        }
        log.debug(String.valueOf(LogMessages.ADD), obj1);
        return obj1;
    }

    //@Override
    public List<E> getAll() {
        log.debug(String.valueOf(LogMessages.GET_ALL_USERS), repository.findAll());
        return repository.findAll();
    }

    public List<E> getAllByOwnerId(Long id) {
        log.debug(String.valueOf(LogMessages.GET_ALL_USERS), repository.findAll());
        return null;
    }

    public E getById(Long objId) {
        if (!repository.findById(objId).isPresent())
            throw new NotFoundException(ExceptionMessages.NOT_OBJECT);

        log.debug(String.valueOf(LogMessages.GET), repository.findById(objId));
        return repository.findById(objId).get();
    }

    public E update(E obj) {
        Long objId = obj.getId();
        if (repository.findById(objId) == null)
            throw new NotFoundException(ExceptionMessages.NOT_OBJECT);

        E savedObj = repository.findById(objId).get();

        E obj1;
        try {
            obj1 = repository.save(obj);
        } catch (Exception e) {
            throw new ConflictException(String.valueOf(HandlerMessages.CONFLICT));
        }

        log.debug(String.valueOf(LogMessages.UPDATE), obj);
        return obj1;
    }

    public E removeById(Long objId) {
        E obj = repository.findById(objId).get();
        repository.deleteById(objId);
        log.debug(String.valueOf(LogMessages.REMOVE), obj);
        return obj;
    }

    @Override
    public void removeALL() {
        repository.deleteAll();
    }

    public List<E> search(String text) {
        return null;
    }


}
