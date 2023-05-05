package ru.practicum.shareit.item.dto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.CommentRepository;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ItemMapper {
    CommentRepository commentRepository;

    @Autowired
    private ItemMapper(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public static Item toItem(ItemDtoReq itemDtoReq, Long ownerId) {
        User owner = new User();
        owner.setId(itemDtoReq.getOwnerId());

        return Item.builder()
                .name(itemDtoReq.getName())
                .description(itemDtoReq.getDescription())
                .available(itemDtoReq.getAvailable())
                .owner(owner)
                .requestId(itemDtoReq.getRequest())
                .build();
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
        List<CommentDtoRes> commentDtoRes = comments.stream().map(e -> CommentMapper.toCommentDtoRes(e)).collect(Collectors.toList());
        itemDtoRes.setComments(commentDtoRes);
        return itemDtoRes;
    }
}
