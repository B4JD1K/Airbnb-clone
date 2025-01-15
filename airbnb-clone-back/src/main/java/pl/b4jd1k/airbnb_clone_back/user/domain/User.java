package pl.b4jd1k.airbnb_clone_back.user.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;
import pl.b4jd1k.airbnb_clone_back.sharedkernel.domain.AbstractAuditingEntity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

// Serializable - interfejs markowy, bez metod,
// - dzięki temu klasa może być serializowana, czyli przekształcana do strumienia bajtów
// - pozwala na zapis obiektów do pliku, przesył przez sieć, przechowywanie w cache lub odczyt
@Entity
@Table(name = "airbnb_user")
public class User extends AbstractAuditingEntity<Long> {

  // @GeneratedValue - sposób generowania wartości 'id',
  // @SequenceGenerator - definiuje generator sekwencji 'user_generator' o alokacji przyrastającej o 1
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userSequenceGenerator")
  @SequenceGenerator(name = "userSequenceGenerator", sequenceName = "user_generator", allocationSize = 1)
  @Column(name = "id")
  private Long id;

  @Column(name = "last_name")
  private String lastName;

  @Column(name = "first_name")
  private String firstName;

  @Column(name = "email")
  private String email;

  @Column(name = "image_url")
  private String imageUrl;

  @UuidGenerator
  @Column(name = "public_id", nullable = false)
  private UUID publicId;

  // relacja w bazie danych pomiędzy 'user_id' a 'authority_name'
  // każdy wiersz reprezentuje relację między użytkownikiem a jego rolą
  @ManyToMany
  @JoinTable(name = "user_authority",
    joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
    inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "name")})
  private Set<Authority> authorities = new HashSet<>();

  @Override
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public UUID getPublicId() {
    return publicId;
  }

  public void setPublicId(UUID publicId) {
    this.publicId = publicId;
  }

  public Set<Authority> getAuthorities() {
    return authorities;
  }

  public void setAuthorities(Set<Authority> authorities) {
    this.authorities = authorities;
  }

  // porównywanie obiektów 'User' - oba są równe jeżeli mają takie same wartości pól
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    User user = (User) o;
    return Objects.equals(lastName, user.lastName) && Objects.equals(firstName, user.firstName) && Objects.equals(email, user.email) && Objects.equals(imageUrl, user.imageUrl) && Objects.equals(publicId, user.publicId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(lastName, firstName, email, imageUrl, publicId);
  }

  @Override
  public String toString() {
    return "User{" +
      "lastName='" + lastName + '\'' +
      ", firstName='" + firstName + '\'' +
      ", email='" + email + '\'' +
      ", imageUrl='" + imageUrl + '\'' +
      ", publicId=" + publicId +
      '}';
  }
}
