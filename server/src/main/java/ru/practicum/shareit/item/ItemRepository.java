package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.abstracts.CommonRepository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends CommonRepository<Item>, QuerydslPredicateExecutor<Item> {
    @Query(" SELECT DISTINCT it FROM ru.practicum.shareit.item.model.Item AS it " +
            " WHERE upper(it.name)        LIKE upper(concat('%', ?1, '%')) " +
            " OR    upper(it.description) LIKE upper(concat('%', ?1, '%')) AND it.available = true")
    List<Item> search(String text);

    boolean existsById(Long id);

    List<Item> findAllByRequestId(Long requestId);

    Optional<Item> findById(Long id);
}
