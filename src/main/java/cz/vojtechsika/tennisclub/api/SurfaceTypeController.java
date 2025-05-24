package cz.vojtechsika.tennisclub.api;

import cz.vojtechsika.tennisclub.dto.SurfaceTypeDTO;
import cz.vojtechsika.tennisclub.dto.SurfaceTypeResponseDTO;
import cz.vojtechsika.tennisclub.service.SurfaceTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/surfaces")
public class SurfaceTypeController {

    private SurfaceTypeService surfaceTypeService;

    @Autowired
    public SurfaceTypeController(SurfaceTypeService theSurfaceTpeService) {
        surfaceTypeService = theSurfaceTpeService;
    }

    @GetMapping("/{id}")
    public SurfaceTypeResponseDTO getSurfaceType(@PathVariable int id) {
        return surfaceTypeService.getById(id);
    }

    @PostMapping("/create")
    public SurfaceTypeResponseDTO createSurfaceType(@RequestBody SurfaceTypeDTO surfaceTypeDTO) {
        return surfaceTypeService.save(surfaceTypeDTO);
    }



}
