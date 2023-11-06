package ru.practicum.shareit.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class CommentDto {
    private long id;
    @NotBlank(message = "Text is mandatory")
    @Size(min = 2, max = 120, message = "Text must be between 2 and 120 characters long")
    private final String text;
    private String authorName;
    private LocalDateTime created;
}
