package pl.b4jd1k.airbnb_clone_back.booking.application.dto;

import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;

public record BookedDateDTO(
  @NotNull OffsetDateTime startDate,
  @NotNull OffsetDateTime endDate) {
}
