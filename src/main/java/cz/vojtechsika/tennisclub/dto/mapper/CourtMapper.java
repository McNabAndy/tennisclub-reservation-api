package cz.vojtechsika.tennisclub.dto.mapper;

import cz.vojtechsika.tennisclub.dto.CourtDTO;
import cz.vojtechsika.tennisclub.dto.response.SurfaceTypeResponseDTO;
import cz.vojtechsika.tennisclub.dto.response.CourtResponseDTO;
import cz.vojtechsika.tennisclub.entity.Court;
import org.springframework.stereotype.Component;

@Component
public class CourtMapper {

    public Court toEntity(CourtDTO courtDTO) {
        Court court = new Court();
        court.setCourtNumber(courtDTO.getCourtNumber());
        court.setDeleted(false);
        return court;
    }

    public CourtResponseDTO toResponseDTO(Court court, SurfaceTypeResponseDTO surfaceTypeResponseDTO) {
        CourtResponseDTO courtResponseDTO = new CourtResponseDTO();
        courtResponseDTO.setCourtNumber(court.getCourtNumber());
        courtResponseDTO.setId(court.getId());
        courtResponseDTO.setSurfaceTypeResponseDTO(surfaceTypeResponseDTO);
        return courtResponseDTO;
    }
}
