package pl.b4jd1k.airbnb_clone_back.listing.presentation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.b4jd1k.airbnb_clone_back.listing.application.TenantService;
import pl.b4jd1k.airbnb_clone_back.listing.application.dto.DisplayCardListingDTO;
import pl.b4jd1k.airbnb_clone_back.listing.domain.BookingCategory;

@RestController
@RequestMapping("/api/tenant-listing")
public class TenantResource {

  private final TenantService tenantService;

  public TenantResource(TenantService tenantService) {
    this.tenantService = tenantService;
  }

  @GetMapping("/get-all-by-category")
  public ResponseEntity<Page<DisplayCardListingDTO>> findAllByBookingCategory(Pageable pageable,
                                                                             @RequestParam BookingCategory bookingCategory) {
    return ResponseEntity.ok(tenantService.getAllByCategory(pageable,bookingCategory));
  }
}
