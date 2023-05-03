package ru.practicum.shareit.item.dto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.CommentRepository;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ItemMapper {
    UserService userService;
    CommentRepository commentRepository;
    CommentMapper commentMapper;

    @Autowired
    private ItemMapper(UserService userService,
                       CommentMapper commentMapper,
                       CommentRepository commentRepository) {
        this.userService = userService;
        this.commentMapper = commentMapper;
        this.commentRepository = commentRepository;
    }

    public Item toItem(ItemDtoReq itemDtoReq, Long ownerId) {
        Item item = new Item();
        item.setName(itemDtoReq.getName());
        item.setDescription(itemDtoReq.getDescription());
        item.setAvailable(itemDtoReq.getAvailable());

        User owner = new User();
        owner.setId(itemDtoReq.getOwnerId());
        item.setOwner(owner);
        item.setRequestId(itemDtoReq.getRequest());
        return item;
    }

    public ItemDtoRes toItemDtoRes(Item item) {
        ItemDtoRes itemDtoRes = new ItemDtoRes();
        itemDtoRes.setId(item.getId());
        itemDtoRes.setName(item.getName());
        itemDtoRes.setDescription(item.getDescription());
        itemDtoRes.setAvailable(item.getAvailable());
        itemDtoRes.setOwnerId(item.getOwner().getId());
        itemDtoRes.setRequest(item.getRequestId());
        List<Comment> comments = commentRepository.findAllByItem(item);
        List<CommentDtoRes> commentDtoRes = comments.stream().map(e -> commentMapper.toCommentDtoRes(e)).collect(Collectors.toList());

        itemDtoRes.setComments(commentDtoRes);


        return itemDtoRes;
    }
}
