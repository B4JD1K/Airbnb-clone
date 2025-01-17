package pl.b4jd1k.airbnb_clone_back.listing.application.dto.vo;

import jakarta.validation.constraints.NotNull;

// VO reprezentują obiekty, które są definiowane wyłącznie przez swoje wartości, a nie przez tożsamość
// Dodatkowo są odpowiedzialne tylko za jedną rzecz – za reprezentowanie wartości i zapewnienie jej poprawności
// 'record' mają automatycznie zaimplementowane metody equals i hashCode
// dzięki temu np. dwa obiekty BathsVO(2) zawsze będą równe, jeśli mają te same wartości
// a tożsamość(encja) ich jest inna, z uwagi że przy ich tworzeniu posiadać będą inne "id"
/**
 *      Cecha         |                 Value Object              |             Encja
 * -------------------|-------------------------------------------|------------------------------------------------
 * Tożsamość    	  | Nie ma tożsamości                         | Ma unikalne ID
 * Definicja równości |	Porównywane na podstawie wartości         | Porównywane na podstawie ID
 * Trwałość	          | Może być częścią encji, ale niekoniecznie |	Zwykle przechowywana w bazie danych jako rekord
 * Zmiana w czasie    | Immutable (niezmienne)                    | Może się zmieniać
 */
public record BathsVO(@NotNull(message = "Bath value must be present") int value) {
}
