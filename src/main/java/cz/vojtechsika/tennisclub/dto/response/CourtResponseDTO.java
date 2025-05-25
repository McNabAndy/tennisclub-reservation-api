package cz.vojtechsika.tennisclub.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourtResponseDTO {

    private Long id;

    private int courtNumber;

    @JsonProperty("surfaceType")
    private SurfaceTypeResponseDTO surfaceTypeResponseDTO;
}
