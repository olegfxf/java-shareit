package ru.practicum.shareit.abstracts;

import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface AbstractStorage<E> {
    E save(E obj);

    List<E> getALL();

    List<E> getAll(Long id);

    E getById(Long id);

    E update(E obj);

    E removeById(Long id);

    void removeALL();
}
