package com.huy.backend.dto.review;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ReviewRequest {
    @NotNull
    private Long movieId;

    @DecimalMin("0.5")
    @DecimalMax("5.0")
    private Double rating;

    @Size(max = 2000)
    private String comment;
}
