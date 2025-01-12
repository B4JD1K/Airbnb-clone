package pl.b4jd1k.airbnb_clone_back.sharedkernel.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.Instant;

@MappedSuperclass // dzięki tej adnotacji właściwości klasy będą dziedziczone przez inne encje, które ją rozszerzają
@EntityListeners(AuditingEntityListener.class) // "nasłuchiwacz" będzie automatycznie aktualizował wartości pól
public abstract class AbstractAuditingEntity<T> implements Serializable {
  // każda klasa, która będzie dziecziczona musi zaimplementować tę metodę. zwraca id encji
  public abstract T getId();

  // podczas zapisu do bazy danych zostanie dodana obecna data utworzenia
  @CreatedDate
  @Column(updatable = false, name = "created_date") // ta kolumna nie będzie możliwa do aktualizacji
  private Instant createdDate = Instant.now();

  // podczas edycji wpisu w bazie danych wartość zostanie zaktualizowana do daty obecnej
  @LastModifiedDate
  @Column(name = "last_modified_date")
  private Instant lastModifiedDate=Instant.now();

  public Instant getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(Instant createdDate) {
    this.createdDate = createdDate;
  }

  public Instant getLastModifiedDate() {
    return lastModifiedDate;
  }

  public void setLastModifiedDate(Instant lastModifiedDate) {
    this.lastModifiedDate = lastModifiedDate;
  }
}
