package cz.vojtechsika.tennisclub.dto.mapper;

import cz.vojtechsika.tennisclub.dto.CourtDTO;
import cz.vojtechsika.tennisclub.entity.Court;
import org.springframework.stereotype.Component;

@Component
public class CourtMapper {

    public Court toEntity(CourtDTO courtDTO) {
        Court court = new Court();
        court.setCourtNumber(courtDTO.getCourtNumber());
        court.setDeleted(false);
        return court; // SurfaceType musím namapovat až na základě vyhledání v service a reservvation je druha strana vazby
    }
}
