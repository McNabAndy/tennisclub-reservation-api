package cz.vojtechsika.tennisclub.api;

import cz.vojtechsika.tennisclub.dto.SurfaceTypeDTO;
import cz.vojtechsika.tennisclub.dto.response.SurfaceTypeResponseDTO;
import cz.vojtechsika.tennisclub.service.SurfaceTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/surfaces")
public class SurfaceTypeController {

    private final SurfaceTypeService surfaceTypeService;

    // Možná to tu všechno převest na ResponseEntity<> a sjednotit pojmenování metod napříč aplikací....

    @Autowired
    public SurfaceTypeController(SurfaceTypeService theSurfaceTpeService) {
        surfaceTypeService = theSurfaceTpeService;
    }

    @PostMapping("/create")
    public SurfaceTypeResponseDTO createSurfaceType(@RequestBody SurfaceTypeDTO surfaceTypeDTO) {
        return surfaceTypeService.saveSurfaceType(surfaceTypeDTO);
    }

    @GetMapping("/{id}")
    public SurfaceTypeResponseDTO getSurfaceType(@PathVariable Long id) {
        return surfaceTypeService.getSurfaceTypeById(id);
    }

    @GetMapping
    public List<SurfaceTypeResponseDTO> getAllSurfaceTypes() {
        return surfaceTypeService.getAllSurfaceTypes();
    }

    @PutMapping("/{id}")
    public SurfaceTypeResponseDTO updateSurfaceType(@PathVariable Long id, @RequestBody SurfaceTypeDTO surfaceTypeDTO) {
        return surfaceTypeService.updateSurfaceType(surfaceTypeDTO, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSurfaceType(@PathVariable Long id) {
        surfaceTypeService.deleteSurfaceType(id);
    }







}
