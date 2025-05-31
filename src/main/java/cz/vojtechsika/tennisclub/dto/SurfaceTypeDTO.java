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


    // tady pracuju s ideckem který nepotřebuju zapomenl sem tento atribut adstranut => mam pak chybu dal v aplikaci, musím proveřit všude kde pracuju s ID surface type
    private Long id;

    private String name;

    private BigDecimal minutePrice;



}
