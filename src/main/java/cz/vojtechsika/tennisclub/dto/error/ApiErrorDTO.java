package cz.vojtechsika.tennisclub.dto.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiErrorDTO {

    // Možna toto POJO nevyužiju uvidíme zatím pro sychr

    private int statusCode;
    private String message;
    private long timestamp;


}
