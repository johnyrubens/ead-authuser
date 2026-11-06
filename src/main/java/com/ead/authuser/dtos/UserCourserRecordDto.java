package com.ead.authuser.dtos;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UserCourserRecordDto(UUID userId,
                                   @NotNull(message = "CourseId is mantatory") UUID courseId) {
}
