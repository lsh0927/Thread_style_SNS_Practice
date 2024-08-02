package com.exam.board.model.user;

import jakarta.validation.constraints.NotEmpty;

public record UserSignUpRequestBody(
       @NotEmpty String username,
       @NotEmpty String password
) {


}
