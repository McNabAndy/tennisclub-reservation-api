package cz.vojtechsika.tennisclub.service;

import cz.vojtechsika.tennisclub.dto.CourtDTO;
import cz.vojtechsika.tennisclub.dto.response.CourtResponseDTO;

import java.util.List;

public interface CourtService {

    CourtResponseDTO save(CourtDTO courtDTO);

    CourtResponseDTO getCourtById(Long id);

    List<CourtResponseDTO> getAllCourts();

    CourtResponseDTO updateCourt(CourtDTO courtDTO, Long id);

    void deleteCourt(Long id);




}
