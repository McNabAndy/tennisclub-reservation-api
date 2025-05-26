package cz.vojtechsika.tennisclub.dto.response;

import cz.vojtechsika.tennisclub.enums.GameType;
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

    private Long id;

    private int courtNumber;

    private String userName;

    private String phoneNumber;

    private String startTime;

    private String endTime;

    private String gameDate;

    private GameType gameType;

    private BigDecimal price;

    private String createdAt;
}
