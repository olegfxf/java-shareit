package ru.practicum.shareit.booking;

import com.sun.jdi.InternalException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.abstracts.AbstractServiceImpl;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.UnexpectedErrorException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.messages.HandlerMessages;
import ru.practicum.shareit.messages.LogMessages;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Transactional
@Slf4j
@Service
public class BookingService extends AbstractServiceImpl<Booking, BookingRepository> {

    ItemService itemService;
    UserService userService;
    UserRepository userRepository;
    ItemRepository itemRepository;
    BookingRepository bookingRepository;
    State state;

    @Autowired
    public BookingService(
            UserRepository userRepository,
            ItemRepository itemRepository,
            BookingRepository bookingRepository,
            ItemService itemService,
            UserService userService) {
        super(bookingRepository);
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
        this.bookingRepository = bookingRepository;
        this.itemService = itemService;
        this.userService = userService;
    }


    @Transactional
    public Booking save(Long bookerId, Booking booking) {
        User booker = userService.getById(bookerId);
        Item item = itemService.getById(booking.getItem().getId());

        if (!item.getAvailable()) // проверка доступности вещи
            throw new ValidationException(String.valueOf(HandlerMessages.VALID));

        dateValidation(booking, item);

        if (booker.getId().equals(item.getOwner().getId())) // владелец веши не может ее бронировать
            throw new NotFoundException(String.valueOf(HandlerMessages.NOT_FOUND));

        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStatus(Status.WAITING);
        log.debug(String.valueOf(LogMessages.TRY_ADD), booking);

        return bookingRepository.save(booking);
    }


    public void dateValidation(Booking booking, Item item) {
        if (booking.getStart() == null || booking.getEnd() == null)
            throw new ValidationException(String.valueOf(HandlerMessages.VALID));

        if (!booking.getEnd().isAfter(booking.getStart()))
            throw new ValidationException(String.valueOf(HandlerMessages.VALID));

        if (booking.getStart().equals(booking.getEnd()))
            throw new ValidationException(String.valueOf(HandlerMessages.VALID));

        if (booking.getStart().isBefore(LocalDateTime.now()))
            throw new ValidationException(String.valueOf(HandlerMessages.VALID));
    }


    @Transactional
    public Booking approveIt(Long bookingId, Long userId, boolean approved) {

        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() ->
                new NotFoundException(String.valueOf(HandlerMessages.NOT_FOUND)));
        Item item = booking.getItem();

        if (!userId.equals(item.getOwner().getId())) // владелец вещи не может ее бронировать
            throw new NotFoundException(String.valueOf(HandlerMessages.NOT_FOUND));

        if (booking.getStatus() == Status.APPROVED)
            throw new ValidationException(String.valueOf(HandlerMessages.VALID));

        booking.setStatus(approved ? Status.APPROVED : Status.REJECTED);
        log.debug(String.valueOf(LogMessages.UPDATE));
        return bookingRepository.save(booking);
    }

    public Booking getById(Long bookingId, Long userId) {
        Booking booking = bookingRepository.findBookingByIdAndBookerOrOwner(bookingId, userId);
        if (booking == null)
            throw new NotFoundException(String.valueOf(HandlerMessages.NOT_FOUND));

        return bookingRepository.findBookingByIdAndBookerOrOwner(bookingId, userId);
    }

    @Transactional
    public List<Booking> getAll1(Long userId, String state) {
        Optional<State> checkState = Stream.of(State.values()).filter(e -> String.valueOf(e).equals(state)).findFirst();
        if (checkState.isEmpty())
            throw new UnexpectedErrorException(String.valueOf(HandlerMessages.UNEXPECTED_ERROR));

        switch (State.valueOf(state)) {
            case ALL:
                return bookingRepository.findAllByBookerOrderByIdDesc(userService.getById(userId));
            case CURRENT:
                return bookingRepository.currentTimeBooker(userId, LocalDateTime.now());
            case PAST:
                return bookingRepository.findAllByBookerAndEndIsBeforeOrderByStartDesc(userService.getById(userId), LocalDateTime.now());
            case FUTURE:
                return bookingRepository.findAllByBookerAndStartIsAfterOrderByStartDesc(userService.getById(userId), LocalDateTime.now());
            case WAITING:
                return bookingRepository.findAllByBookerAndStatusOrderByStartDesc(userService.getById(userId), Status.WAITING);
            case REJECTED:
                return bookingRepository.findAllByBookerAndStatusOrderByStartDesc(userService.getById(userId), Status.REJECTED);
        }
        return null;
    }


    @Transactional
    public List<Booking> ownerGet(Long userId, String state) throws IllegalArgumentException {
        Optional<State> checkState = Stream.of(State.values()).filter(e -> String.valueOf(e).equals(state)).findFirst();
        if (checkState.isEmpty())
            throw new UnexpectedErrorException(String.valueOf(HandlerMessages.UNEXPECTED_ERROR));

        switch (State.valueOf(state)) {
            case ALL:
                if (!userRepository.existsById(userId))
                    throw new InternalException(String.valueOf(HandlerMessages.SERVER_ERROR));
                return bookingRepository.findAll(userId);
            case CURRENT:
                return bookingRepository.currentTimeOwner(userId, LocalDateTime.now());
            case PAST:
                return bookingRepository.findPast(userId, LocalDateTime.now());
            case FUTURE:
                return bookingRepository.findFuture(userId, LocalDateTime.now());
            case WAITING:
                return bookingRepository.findByStatus(userId, String.valueOf(Status.WAITING));
            case REJECTED:
                return bookingRepository.findByStatus(userId, String.valueOf(Status.REJECTED));
        }
        return null;
    }
}
