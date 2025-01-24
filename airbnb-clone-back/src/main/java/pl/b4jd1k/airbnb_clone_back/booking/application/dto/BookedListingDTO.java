package pl.b4jd1k.airbnb_clone_back.booking.application.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import pl.b4jd1k.airbnb_clone_back.listing.application.dto.sub.PictureDTO;
import pl.b4jd1k.airbnb_clone_back.listing.application.dto.vo.PriceVO;

import java.util.UUID;

public record BookedListingDTO(@Valid PictureDTO cover,
                               @NotNull String location,
                               @Valid BookedDateDTO dates,
                               @Valid PriceVO totalPrice,
                               @NotNull UUID bookingPublicId,
                               @NotNull UUID listingPublicId) {
}
