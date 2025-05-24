package cz.vojtechsika.tennisclub.dto.mapper;

import cz.vojtechsika.tennisclub.dto.SurfaceTypeDTO;
import cz.vojtechsika.tennisclub.dto.SurfaceTypeResponseDTO;
import cz.vojtechsika.tennisclub.entity.SurfaceType;
import org.springframework.stereotype.Component;

@Component
public class SurfaceTypeMapper {

    public SurfaceType toEntity(SurfaceTypeDTO surfaceTypeDTO) {
        SurfaceType surfaceType = new SurfaceType();
        surfaceType.setName(surfaceTypeDTO.getName());
        surfaceType.setDeleted(false);
        surfaceType.setMinutePrice(surfaceTypeDTO.getMinutePrice());
        return surfaceType; // Curts ne nastaví až na základě vazny, id  nastavuje databaze sama

    }

    public SurfaceTypeResponseDTO toDTO(SurfaceType surfaceType) {
        SurfaceTypeResponseDTO surfaceTypeResponseDTO = new SurfaceTypeResponseDTO();
        surfaceTypeResponseDTO.setId(surfaceType.getId());
        surfaceTypeResponseDTO.setName(surfaceType.getName());
        surfaceTypeResponseDTO.setMinutePrice(surfaceType.getMinutePrice());
        return surfaceTypeResponseDTO;
    }
}
