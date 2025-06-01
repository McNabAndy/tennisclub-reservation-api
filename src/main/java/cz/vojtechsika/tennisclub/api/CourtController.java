package cz.vojtechsika.tennisclub.api;

import cz.vojtechsika.tennisclub.dto.CourtDTO;
import cz.vojtechsika.tennisclub.dto.response.CourtResponseDTO;
import cz.vojtechsika.tennisclub.service.CourtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/courts")
public class CourtController {

    private final CourtService courtService;

    @Autowired
    public CourtController(CourtService theCourtService) {
        courtService = theCourtService;
    }


    @PostMapping("/create")
    public ResponseEntity<CourtResponseDTO> createCourt(@RequestBody CourtDTO courtDTO) {
        return new ResponseEntity<>(courtService.save(courtDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourtResponseDTO> getCourt(@PathVariable Long id) {
        return new ResponseEntity<>(courtService.getCourtById(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<CourtResponseDTO>> getAllCourts() {
        return new ResponseEntity<>(courtService.getAllCourts(), HttpStatus.OK);
    }


    @PutMapping("/{id}")
    public ResponseEntity<CourtResponseDTO> updateCourt(@PathVariable Long id, @RequestBody CourtDTO courtDTO) {
        return new ResponseEntity<>(courtService.updateCourt(courtDTO, id), HttpStatus.OK);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteCourt(@PathVariable Long id) {
        courtService.deleteCourt(id);
        Map<String, String> response = Map.of("message", "Court with id " + id + " was deleted");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }







}
