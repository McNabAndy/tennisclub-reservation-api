package cz.vojtechsika.tennisclub.dto.mapper;

import cz.vojtechsika.tennisclub.dto.CourtDTO;
import cz.vojtechsika.tennisclub.dto.response.SurfaceTypeResponseDTO;
import cz.vojtechsika.tennisclub.dto.response.CourtResponseDTO;
import cz.vojtechsika.tennisclub.entity.Court;
import org.springframework.stereotype.Component;
/**
 * CourtMapper is responsible for converting between {@link CourtDTO} and {@link Court} entities,
 * as well as building {@link CourtResponseDTO} objects to be returned in API responses.
 * It encapsulates the logic for mapping court-related data structures.
 */
@Component
public class CourtMapper {


    /**
     * Converts a {@link CourtDTO} to a new {@link Court} entity.
     * The created entity will have its court number set from the DTO and
     * its deleted flag initialized to {@code false}.
     *
     * @param courtDTO The {@link CourtDTO} containing court data from the client.
     * @return A new {@link Court} entity with values populated from {@code courtDTO}.
     */
    public Court toEntity(CourtDTO courtDTO) {
        Court court = new Court();
        court.setCourtNumber(courtDTO.getCourtNumber());
        court.setDeleted(false);
        return court;
    }


    /**
     * Builds a {@link CourtResponseDTO} from a {@link Court} entity and an associated
     * {@link SurfaceTypeResponseDTO}. The response DTO will include the court's ID, court number,
     * and embedded surface type information.
     *
     * @param court The {@link Court} entity retrieved from the database.
     * @param surfaceTypeResponseDTO The {@link SurfaceTypeResponseDTO} containing surface type details.
     * @return A {@link CourtResponseDTO} combining court entity data with its surface type DTO.
     */
    public CourtResponseDTO toResponseDTO(Court court, SurfaceTypeResponseDTO surfaceTypeResponseDTO) {
        CourtResponseDTO courtResponseDTO = new CourtResponseDTO();
        courtResponseDTO.setCourtNumber(court.getCourtNumber());
        courtResponseDTO.setId(court.getId());
        courtResponseDTO.setSurfaceTypeResponseDTO(surfaceTypeResponseDTO);
        return courtResponseDTO;
    }
}
