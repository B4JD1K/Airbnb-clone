package pl.b4jd1k.airbnb_clone_back.listing.domain;

import jakarta.persistence.*;
import pl.b4jd1k.airbnb_clone_back.sharedkernel.domain.AbstractAuditingEntity;

import java.util.Arrays;
import java.util.Objects;

@Entity
@Table(name = "listing_picture")
public class ListingPicture extends AbstractAuditingEntity<Long> {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "listingPictureSequenceGenerator")
  @SequenceGenerator(name = "listingPictureSequenceGenerator", sequenceName = "listing_picture_generator", allocationSize = 1)
  @Column(name = "id")
  private Long id;

  @ManyToOne // wiele ListingPicture może być powiązanych z jednym Listing
  @JoinColumn(name = "listing_fk", referencedColumnName = "id") // listing foreign key wskazuje kolumnę w tabeli listing
  private Listing listing;

  @Lob // dla pliku binarnego, bloba itp - Lob = Large Object, będzie przechowywany w tablicy bajtów
  @Column(name = "file", nullable = false)
  private byte[] file;

  // określa typ pliku (np. image/jpeg czy image/png)
  @Column(name = "file_content_type")
  private String fileContentType;

  // czy dane zdjęcie jest okładką
  @Column(name = "is_cover")
  private boolean isCover;

  @Override
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Listing getListing() {
    return listing;
  }

  public void setListing(Listing listing) {
    this.listing = listing;
  }

  public byte[] getFile() {
    return file;
  }

  public void setFile(byte[] file) {
    this.file = file;
  }

  public String getFileContentType() {
    return fileContentType;
  }

  public void setFileContentType(String fileContentType) {
    this.fileContentType = fileContentType;
  }

  public boolean isCover() {
    return isCover;
  }

  public void setCover(boolean cover) {
    isCover = cover;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    ListingPicture that = (ListingPicture) o;
    return isCover == that.isCover && Objects.deepEquals(file, that.file) && Objects.equals(fileContentType, that.fileContentType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(Arrays.hashCode(file), fileContentType, isCover);
  }
}
