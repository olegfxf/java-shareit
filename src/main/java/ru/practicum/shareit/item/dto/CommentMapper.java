package ru.practicum.shareit.item.dto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.CommentRepository;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.user.UserService;

@Component
public class CommentMapper {
    UserService userService;
    ItemRepository itemRepository;
    CommentRepository commentRepository;

    @Autowired
    private CommentMapper(UserService userService,
                          CommentRepository commentRepository,
                          ItemRepository itemRepository) {
        this.userService = userService;
        this.commentRepository = commentRepository;
        this.itemRepository = itemRepository;
    }

    public Comment toComment(CommentDtoReq commentDtoReq) {
        Comment comment = new Comment();
        comment.setText(commentDtoReq.getText());
        return comment;
    }

    public CommentDtoRes toCommentDtoRes(Comment comment) {
        CommentDtoRes commentDtoRes = new CommentDtoRes();
        commentDtoRes.setId(comment.getId());
        commentDtoRes.setText(comment.getText());
        commentDtoRes.setAuthorName(comment.getAuthor().getName());
        commentDtoRes.setCreated(comment.getCreated());
        return commentDtoRes;
    }
}
