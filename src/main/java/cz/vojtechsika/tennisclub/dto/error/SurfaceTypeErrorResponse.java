package cz.vojtechsika.tennisclub.dto.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SurfaceTypeErrorResponse {

    private int statusCode;
    private String message;
    private long timestamp;


}
