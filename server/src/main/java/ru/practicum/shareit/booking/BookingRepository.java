package ru.practicum.shareit.booking;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.abstracts.CommonRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends CommonRepository<Booking> {

    /**
     * для заданного пользователя находим записи бронирования, где он хозяин вещи
     * или забронировал вещь другого пользователя
     */
    @Query(value = "SELECT * FROM Bookings AS b " +
            " INNER JOIN Items AS i ON b.item_id = i.id " +
            " WHERE b.id = :bookerId " +
            "   AND (b.booker_id = :ownerId OR i.owner_id = :ownerId)", nativeQuery = true)
    Booking findBookingByIdAndBookerOrOwner(@Param("bookerId") Long bookingId, @Param("ownerId") Long ownerId);

    /**
     * находим все записи для пользователя, где он хозяина вещи или ее забронировал
     */
    List<Booking> findAllByBookerOrderByIdDesc(User booker, Pageable pageable);


    @Query(value = " SELECT * FROM Bookings AS b " +
            " INNER JOIN Items AS i on b.item_id = i.id " +
            " WHERE i.owner_id = :ownerId " +
            " ORDER BY b.start_date DESC ", nativeQuery = true)
    List<Booking> findAll(@Param("ownerId") Long ownerId, Pageable pageable);


    /**
     * записи бронирования пользователя-booker / пользователя-owner с заданным статусом
     */
    List<Booking> findAllByBookerAndStatusOrderByStartDesc(User user, Status status);

    @Query(value = " SELECT * FROM Bookings AS b " +
            " INNER JOIN Items AS i on b.item_id = i.id " +
            " WHERE b.status = :status " +
            " AND i.owner_id = :ownerId " +
            " ORDER BY b.start_date DESC ", nativeQuery = true)
    List<Booking> findByStatus(@Param("ownerId") Long ownerId, @Param("status") String status);

    /**
     * записи бронирования пользователя-booker / пользователя-owner после указанной даты
     */
    List<Booking> findAllByBookerAndStartIsAfterOrderByStartDesc(User booker, LocalDateTime dateTime);

    @Query(value = " SELECT * FROM Bookings AS b " +
            " INNER JOIN Items AS i ON b.item_id = i.id " +
            " WHERE i.owner_id = :ownerId " +
            " AND :dateTime <> b.start_date " +
            " ORDER BY b.start_date DESC", nativeQuery = true)
    List<Booking> findFuture(@Param("ownerId") Long ownerId, @Param("dateTime") LocalDateTime dateTime);

    /**
     * записи бронирования пользователя-booker / пользователя-owner до указанной даты
     */
    List<Booking> findAllByBookerAndEndIsBeforeOrderByStartDesc(User user, LocalDateTime dateTime);

    @Query(value = " SELECT * FROM Bookings AS b " +
            " INNER JOIN Items AS i ON b.item_id = i.id " +
            " WHERE i.owner_id = :ownerId " +
            " AND :dateTime > b.end_date " +
            " ORDER BY b.start_date DESC", nativeQuery = true)
    List<Booking> findPast(@Param("ownerId") Long ownerId, @Param("dateTime") LocalDateTime dateTime);

    /**
     * записи бронирования пользователя-booker / пользователя-owner на текущую дату
     */
    @Query(value = "SELECT * FROM Bookings AS b " +
            " WHERE b.start_date < :now AND :now < b.end_date AND b.booker_id = :bookerId ", nativeQuery = true)
    List<Booking> currentTimeBooker(@Param("bookerId") Long bookerId, @Param("now") LocalDateTime now);

    @Query(value = "SELECT * FROM Bookings AS b " +
            " INNER JOIN Items AS i ON b.item_id = i.id " +
            " WHERE b.start_date < :now AND :now < b.end_date AND i.owner_id = :ownerId ", nativeQuery = true)
    List<Booking> currentTimeOwner(@Param("ownerId") Long ownerId, @Param("now") LocalDateTime now);


    @Query(value = "SELECT * FROM Bookings AS b " +
            " INNER JOIN Items AS i ON b.item_id = i.id " +
            " WHERE b.start_date < :now " +
            "AND b.item_id = :ids " +
            "AND i.owner_id = :userId " +
            "AND b.status = 'APPROVED' " +
            " ORDER BY b.start_date DESC limit 1", nativeQuery = true)
    Booking findBookingsLast(@Param("ids") Long ids,
                             @Param("now") LocalDateTime now,
                             @Param("userId") Long userId);


    @Query(value = "SELECT * FROM Bookings AS b " +
            " INNER JOIN Items AS i ON b.item_id = i.id " +
            " WHERE b.start_date > :now " +
            " AND b.item_id = :ids " +
            " AND i.owner_id = :userId " +
            " AND b.status = 'APPROVED' " +
            " ORDER BY b.start_date ASC limit 1", nativeQuery = true)
    Booking findBookingsNext(@Param("ids") Long ids,
                             @Param("now") LocalDateTime now,
                             @Param("userId") Long userId);

    boolean existsBookingByBookerAndItemAndStatus(User user, Item item, Status status);

    List<Booking> findByBookerAndItemAndEndBefore(User booker, Item item, LocalDateTime end);


}
