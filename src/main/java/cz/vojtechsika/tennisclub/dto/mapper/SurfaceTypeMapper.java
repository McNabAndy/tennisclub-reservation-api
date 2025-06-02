package cz.vojtechsika.tennisclub.dto.mapper;

import cz.vojtechsika.tennisclub.dto.SurfaceTypeDTO;
import cz.vojtechsika.tennisclub.dto.response.SurfaceTypeResponseDTO;
import cz.vojtechsika.tennisclub.entity.SurfaceType;
import org.springframework.stereotype.Component;

/**
 * SurfaceTypeMapper is responsible for converting between {@link SurfaceTypeDTO} and {@link SurfaceType} entities,
 * as well as building {@link SurfaceTypeResponseDTO} objects for API responses. It encapsulates the mapping logic
 * for surface type–related data structures.
 *
 * When converting from DTO to entity, the {@code deleted} flag is initialized to {@code false}.
 */
@Component
public class SurfaceTypeMapper {

    /**
     * Converts a {@link SurfaceTypeDTO} to a new {@link SurfaceType} entity. The created entity will have its
     * name and minute price set from the DTO, and its {@code deleted} flag initialized to {@code false}.
     *
     * @param surfaceTypeDTO The {@link SurfaceTypeDTO} containing surface type data from the client.
     * @return A new {@link SurfaceType} entity populated with values from {@code surfaceTypeDTO}.
     */
    public SurfaceType toEntity(SurfaceTypeDTO surfaceTypeDTO) {
        SurfaceType surfaceType = new SurfaceType();
        surfaceType.setName(surfaceTypeDTO.getName());
        surfaceType.setDeleted(false);
        surfaceType.setMinutePrice(surfaceTypeDTO.getMinutePrice());
        return surfaceType;
    }


    /**
     * Builds a {@link SurfaceTypeResponseDTO} from a {@link SurfaceType} entity. The response DTO will include
     * the surface type's ID, name, and minute price.
     *
     * @param surfaceType The {@link SurfaceType} entity retrieved from the database.
     * @return A {@link SurfaceTypeResponseDTO} containing data formatted for client consumption.
     */
    public SurfaceTypeResponseDTO toResponseDTO(SurfaceType surfaceType) {
        SurfaceTypeResponseDTO surfaceTypeResponseDTO = new SurfaceTypeResponseDTO();
        surfaceTypeResponseDTO.setId(surfaceType.getId());
        surfaceTypeResponseDTO.setName(surfaceType.getName());
        surfaceTypeResponseDTO.setMinutePrice(surfaceType.getMinutePrice());
        return surfaceTypeResponseDTO;
    }
}
