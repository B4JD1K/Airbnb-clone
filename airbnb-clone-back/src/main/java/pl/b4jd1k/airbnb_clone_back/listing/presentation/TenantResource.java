package pl.b4jd1k.airbnb_clone_back.listing.presentation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.b4jd1k.airbnb_clone_back.listing.application.TenantService;
import pl.b4jd1k.airbnb_clone_back.listing.application.dto.DisplayCardListingDTO;
import pl.b4jd1k.airbnb_clone_back.listing.application.dto.DisplayListingDTO;
import pl.b4jd1k.airbnb_clone_back.listing.domain.BookingCategory;
import pl.b4jd1k.airbnb_clone_back.sharedkernel.service.State;
import pl.b4jd1k.airbnb_clone_back.sharedkernel.service.StatusNotification;

import java.util.UUID;

@RestController
@RequestMapping("/api/tenant-listing")
public class TenantResource {

  private final TenantService tenantService;

  public TenantResource(TenantService tenantService) {
    this.tenantService = tenantService;
  }

  @GetMapping("/get-all-by-category")
  public ResponseEntity<Page<DisplayCardListingDTO>> findAllByBookingCategory(Pageable pageable,
                                                                             @RequestParam BookingCategory category) {
    return ResponseEntity.ok(tenantService.getAllByCategory(pageable, category));
  }

  @GetMapping("/get-one")
  public ResponseEntity<DisplayListingDTO> getOne(@RequestParam UUID publicId) {
    State<DisplayListingDTO, String> displayListingState = tenantService.getOne(publicId);
    if (displayListingState.getStatus().equals(StatusNotification.OK)) {
      return ResponseEntity.ok(displayListingState.getValue());
    } else {
      ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, displayListingState.getError());
      return ResponseEntity.of(problemDetail).build();
    }
  }
}
