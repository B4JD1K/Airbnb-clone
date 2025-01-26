package pl.b4jd1k.airbnb_clone_back.listing.application.dto.sub;

import pl.b4jd1k.airbnb_clone_back.listing.application.dto.vo.DescriptionVO;
import pl.b4jd1k.airbnb_clone_back.listing.application.dto.vo.TitleVO;
import jakarta.validation.constraints.NotNull;

public record DescriptionDTO(
        @NotNull TitleVO title,
        @NotNull DescriptionVO description
        ) {
}
