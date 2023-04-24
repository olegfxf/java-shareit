package ru.practicum.shareit.abstracts;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.messages.ExceptionMessages;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@Component
public abstract class AbstractDLAStorage<E extends AbstractModel> implements AbstractStorage {
    public final Map<Long, E> mem = new HashMap<>();

    public Long id = 1L;

    @Override
    public E save(Object obj) {
        ((E) obj).setId(id++);
        mem.put(((E) obj).getId(), (E) obj);
        return (E) obj;
    }

    @Override
    public List<E> getALL() {
        return mem.values().stream().collect(toList());
    }

    @Override
    public List<E> getAll(Long id) {
        return null;
    }

    @Override
    public E getById(Long id) {
        return mem.get(id);
    }

    @Override
    public E update(Object obj) {
        Long id = ((E) obj).getId();
        if (mem.get(id) == null)
            throw new NotFoundException(ExceptionMessages.NOT_FOUND_ID);
        mem.put(id, (E) obj);
        return (E) obj;
    }

    public E removeById(Long id) {
        return mem.remove(id);
    }

    public void removeALL() {
        mem.clear();
    }

}
