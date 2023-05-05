package ru.practicum.shareit.item.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Comment;

@Component
public class CommentMapper {
    public static Comment toComment(CommentDtoReq commentDtoReq) {
        return Comment.builder()
                .text(commentDtoReq.getText()).build();
    }

    public static CommentDtoRes toCommentDtoRes(Comment comment) {
        return CommentDtoRes.builder()
                .id(comment.getId())
                .text(comment.getText())
                .authorName(comment.getAuthor().getName())
                .created(comment.getCreated())
                .build();
    }
}
