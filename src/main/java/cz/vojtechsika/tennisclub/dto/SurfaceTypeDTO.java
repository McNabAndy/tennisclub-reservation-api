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
public class SurfaceTypeDTO {

    private Long id;

    private String name;

    private BigDecimal minutePrice;



}
