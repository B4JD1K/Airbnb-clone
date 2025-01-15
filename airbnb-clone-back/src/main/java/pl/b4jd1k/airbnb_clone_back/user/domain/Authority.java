package pl.b4jd1k.airbnb_clone_back.user.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Objects;

@Entity
@Table(name="authority")
public class Authority {

  @NotNull
  @Size(max=50)
  @Id
  @Column(length = 50)
  private String name; // nazwa autoryzacji (admin/user)

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  // portównywanie obiektów Authority
  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    Authority authority = (Authority) o;
    return Objects.equals(name, authority.name);
  }

  // generowanie skróconego kodu obiektu na podstawie pola 'name'
  // Jest to zgodne z zasadą, że obiekt o tej samej nazwie powinien mieć ten sam kod skrócony.
  // Dzięki temu obiekty tej samej klasy z identyczną nazwą będą traktowane jako równe.
  @Override
  public int hashCode() {
    return Objects.hashCode(name);
  }

  @Override
  public String toString() {
    return "Authority{" +
      "name='" + name + '\'' +
      '}';
  }
}
