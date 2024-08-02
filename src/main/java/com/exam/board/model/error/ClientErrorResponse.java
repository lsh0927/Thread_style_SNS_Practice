package com.exam.board.model.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ClientErrorResponse(
        HttpStatus status, Object message
) {
}
