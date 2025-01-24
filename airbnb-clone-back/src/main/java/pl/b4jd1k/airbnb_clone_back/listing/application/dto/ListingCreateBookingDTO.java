package pl.b4jd1k.airbnb_clone_back.listing.application.dto;

import pl.b4jd1k.airbnb_clone_back.listing.application.dto.vo.PriceVO;

import java.util.UUID;

public record ListingCreateBookingDTO(
  UUID listingPublicId, PriceVO price) {

}
