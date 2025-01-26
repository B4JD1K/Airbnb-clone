package pl.b4jd1k.airbnb_clone_back.listing.application.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import pl.b4jd1k.airbnb_clone_back.booking.application.dto.BookedDateDTO;
import pl.b4jd1k.airbnb_clone_back.listing.application.dto.sub.ListingInfoDTO;

public record SearchDTO(@Valid BookedDateDTO dates,
                        @Valid ListingInfoDTO infos,
                        @NotEmpty String location) {
}
