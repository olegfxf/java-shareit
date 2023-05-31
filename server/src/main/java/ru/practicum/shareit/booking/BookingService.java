package ru.practicum.shareit.booking;

import com.sun.jdi.InternalException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDtoRes;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.UnexpectedErrorException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.messages.HandlerMessages;
import ru.practicum.shareit.messages.LogMessages;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

//@Transactional(readOnly = true)
@Slf4j
@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class BookingService {

    ItemService itemService;
    UserService userService;
    UserRepository userRepository;
    BookingRepository bookingRepository;


    @Transactional
    public BookingDtoRes save(Long bookerId, Booking booking) {
        User booker = userService.getById(bookerId);
        Item item = itemService.getById(booking.getItem().getId());

        if (!item.getAvailable())
            throw new ValidationException(String.valueOf(HandlerMessages.VALID));

        dateValidation(booking, item);

        if (booker.getId().equals(item.getOwner().getId())) // владелец веши не может ее бронировать
            throw new NotFoundException(String.valueOf(HandlerMessages.NOT_FOUND));

        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStatus(Status.WAITING);
        log.debug(String.valueOf(LogMessages.TRY_ADD), booking);

        return BookingMapper.toBookingDtoRes(bookingRepository.save(booking));
    }


    public void dateValidation(Booking booking, Item item) {
        if (booking.getStart() == null || booking.getEnd() == null)
            throw new ValidationException(String.valueOf(HandlerMessages.VALID));

        if (booking.getStart().equals(booking.getEnd())) {
            throw new ValidationException(String.valueOf(HandlerMessages.VALID));
        }

        if (!booking.getEnd().isAfter(booking.getStart()))
            throw new ValidationException(String.valueOf(HandlerMessages.VALID));

        if (booking.getStart().isBefore(LocalDateTime.now()))
            throw new ValidationException(String.valueOf(HandlerMessages.VALID));
    }


    @Transactional
    public BookingDtoRes approveIt(Long bookingId, Long userId, boolean approved) {

        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() ->
                new NotFoundException(String.valueOf(HandlerMessages.NOT_FOUND)));
        Item item = booking.getItem();

        if (!userId.equals(item.getOwner().getId()))
            throw new NotFoundException(String.valueOf(HandlerMessages.NOT_FOUND));

        if (booking.getStatus() == Status.APPROVED)
            throw new ValidationException(String.valueOf(HandlerMessages.VALID));

        booking.setStatus(approved ? Status.APPROVED : Status.REJECTED);
        log.debug(String.valueOf(LogMessages.UPDATE));
        return BookingMapper.toBookingDtoRes(bookingRepository.save(booking));
    }

    public BookingDtoRes getById(Long bookingId, Long userId) {
        Booking booking = bookingRepository.findBookingByIdAndBookerOrOwner(bookingId, userId);
        if (booking == null)
            throw new NotFoundException(String.valueOf(HandlerMessages.NOT_FOUND));

        return BookingMapper.toBookingDtoRes(bookingRepository.findBookingByIdAndBookerOrOwner(bookingId, userId));
    }

    @Transactional
    public List<BookingDtoRes> getAll(Long userId, String state, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from > 0 & size > 0 ? from / size : 0, size);
        Optional<State> checkState = Stream.of(State.values()).filter(e -> String.valueOf(e).equals(state)).findFirst();
        if (checkState.isEmpty())
            throw new UnexpectedErrorException(String.valueOf(HandlerMessages.UNEXPECTED_ERROR));

        switch (State.valueOf(state)) {
            case ALL:
                return bookingRepository.findAllByBookerOrderByIdDesc(userService.getById(userId), pageable)
                        .stream().map(e -> BookingMapper.toBookingDtoRes(e)).collect(Collectors.toList());
            case CURRENT:
                return bookingRepository.currentTimeBooker(userId, LocalDateTime.now())
                        .stream().map(e -> BookingMapper.toBookingDtoRes(e)).collect(Collectors.toList());
            case PAST:
                return bookingRepository.findAllByBookerAndEndIsBeforeOrderByStartDesc(userService.getById(userId), LocalDateTime.now())
                        .stream().map(e -> BookingMapper.toBookingDtoRes(e)).collect(Collectors.toList());
            case FUTURE:
                return bookingRepository.findAllByBookerAndStartIsAfterOrderByStartDesc(userService.getById(userId), LocalDateTime.now())
                        .stream().map(e -> BookingMapper.toBookingDtoRes(e)).collect(Collectors.toList());
            case WAITING:
                return bookingRepository.findAllByBookerAndStatusOrderByStartDesc(userService.getById(userId), Status.WAITING)
                        .stream().map(e -> BookingMapper.toBookingDtoRes(e)).collect(Collectors.toList());
            case REJECTED:
                return bookingRepository.findAllByBookerAndStatusOrderByStartDesc(userService.getById(userId), Status.REJECTED)
                        .stream().map(e -> BookingMapper.toBookingDtoRes(e)).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }


    @Transactional
    public List<BookingDtoRes> ownerGet(Long userId, String state, Integer from, Integer size) throws IllegalArgumentException {
        PageRequest pageRequest = PageRequest.of(from, size);
        Optional<State> checkState = Stream.of(State.values()).filter(e -> String.valueOf(e).equals(state)).findFirst();
        if (checkState.isEmpty())
            throw new UnexpectedErrorException(String.valueOf(HandlerMessages.UNEXPECTED_ERROR));

        switch (State.valueOf(state)) {
            case ALL:
                if (!userRepository.existsById(userId))
                    throw new InternalException(String.valueOf(HandlerMessages.SERVER_ERROR));
                return bookingRepository.findAll(userId, pageRequest).stream()
                        .map(e -> BookingMapper.toBookingDtoRes(e)).collect(Collectors.toList());
            case CURRENT:
                return bookingRepository.currentTimeOwner(userId, LocalDateTime.now()).stream()
                        .map(e -> BookingMapper.toBookingDtoRes(e)).collect(Collectors.toList());
            case PAST:
                return bookingRepository.findPast(userId, LocalDateTime.now()).stream()
                        .map(e -> BookingMapper.toBookingDtoRes(e)).collect(Collectors.toList());
            case FUTURE:
                return bookingRepository.findFuture(userId, LocalDateTime.now()).stream()
                        .map(e -> BookingMapper.toBookingDtoRes(e)).collect(Collectors.toList());
            case WAITING:
                return bookingRepository.findByStatus(userId, String.valueOf(Status.WAITING)).stream()
                        .map(e -> BookingMapper.toBookingDtoRes(e)).collect(Collectors.toList());
            case REJECTED:
                return bookingRepository.findByStatus(userId, String.valueOf(Status.REJECTED)).stream()
                        .map(e -> BookingMapper.toBookingDtoRes(e)).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
