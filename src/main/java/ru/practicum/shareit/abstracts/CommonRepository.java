package ru.practicum.shareit.abstracts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface CommonRepository<E> extends JpaRepository<E, Long> {
    List<E> findAll();
}
