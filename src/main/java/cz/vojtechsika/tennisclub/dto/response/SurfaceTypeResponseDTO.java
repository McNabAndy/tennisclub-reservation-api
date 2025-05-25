package cz.vojtechsika.tennisclub.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SurfaceTypeResponseDTO {

    private Long id;

    private String name;

    private BigDecimal minutePrice;
}
