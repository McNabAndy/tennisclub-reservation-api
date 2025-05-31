package cz.vojtechsika.tennisclub.api;

import cz.vojtechsika.tennisclub.dto.SurfaceTypeDTO;
import cz.vojtechsika.tennisclub.dto.response.SurfaceTypeResponseDTO;
import cz.vojtechsika.tennisclub.service.SurfaceTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/surfaces")
public class SurfaceTypeController {

    private final SurfaceTypeService surfaceTypeService;


    @Autowired
    public SurfaceTypeController(SurfaceTypeService theSurfaceTpeService) {
        surfaceTypeService = theSurfaceTpeService;
    }

    @PostMapping(value= "/create",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SurfaceTypeResponseDTO> createSurfaceType(@RequestBody SurfaceTypeDTO surfaceTypeDTO) {
        return new ResponseEntity<>(surfaceTypeService.saveSurfaceType(surfaceTypeDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SurfaceTypeResponseDTO> getSurfaceType(@PathVariable Long id) {
        return new ResponseEntity<>(surfaceTypeService.getSurfaceTypeById(id),HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<SurfaceTypeResponseDTO>> getAllSurfaceTypes() {
        return new ResponseEntity<>(surfaceTypeService.getAllSurfaceTypes(), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SurfaceTypeResponseDTO> updateSurfaceType(@PathVariable Long id,
                                                                    @RequestBody SurfaceTypeDTO surfaceTypeDTO) {
        return new ResponseEntity<>(surfaceTypeService.updateSurfaceType(surfaceTypeDTO, id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteSurfaceType(@PathVariable Long id) {
        surfaceTypeService.deleteSurfaceType(id);
        Map<String, String> response = Map.of("message", "Surface Type with ID " + id + " was deleted.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }







}
