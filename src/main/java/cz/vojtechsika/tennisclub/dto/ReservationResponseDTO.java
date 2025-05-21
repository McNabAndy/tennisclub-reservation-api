package cz.vojtechsika.tennisclub.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationResponseDTO {

    // sem si pak přidám další informace k odpvedi jako ID reseravce atd, číslo kurtu, a datum a čas
    private BigDecimal price;
}
