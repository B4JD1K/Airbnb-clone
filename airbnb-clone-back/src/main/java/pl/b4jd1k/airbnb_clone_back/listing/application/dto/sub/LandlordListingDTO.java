package pl.b4jd1k.airbnb_clone_back.listing.application.dto.sub;

import jakarta.validation.constraints.NotNull;

// przechowuje dane wynajmującego - imię i avatar
public record LandlordListingDTO(@NotNull String firstname,
                                 @NotNull String imageUrl) {
}
