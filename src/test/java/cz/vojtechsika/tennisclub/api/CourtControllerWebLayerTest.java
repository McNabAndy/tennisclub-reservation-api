package cz.vojtechsika.tennisclub.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.vojtechsika.tennisclub.dto.CourtDTO;
import cz.vojtechsika.tennisclub.dto.response.CourtResponseDTO;
import cz.vojtechsika.tennisclub.dto.response.SurfaceTypeResponseDTO;
import cz.vojtechsika.tennisclub.exception.CourtNotFoundException;
import cz.vojtechsika.tennisclub.exception.CourtNumberAlreadyExistsException;
import cz.vojtechsika.tennisclub.exception.SurfaceTypeNotFoundException;
import cz.vojtechsika.tennisclub.service.CourtService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@WebMvcTest(controllers = CourtController.class)
class CourtControllerWebLayerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CourtService courtService;

    @Autowired
    private final ObjectMapper objectMapper = new ObjectMapper();


    @Test
    @DisplayName("Create court")
    void createCourt_validData_returnCourtDetail() throws Exception {

        // Arrange
        int courtNumber = 101;
        Long surfaceTypeId = 1L;
        Long courtId = 2L;

        CourtDTO courtDTO = new CourtDTO();
        courtDTO.setCourtNumber(courtNumber);
        courtDTO.setSurfaceTypeId(surfaceTypeId);

        SurfaceTypeResponseDTO surfaceTypeResponseDTO = new SurfaceTypeResponseDTO();
        surfaceTypeResponseDTO.setId(surfaceTypeId);
        surfaceTypeResponseDTO.setName("Grass");

        CourtResponseDTO courtResponseDTO = new CourtResponseDTO();
        courtResponseDTO.setId(courtId);
        courtResponseDTO.setCourtNumber(courtNumber);
        courtResponseDTO.setSurfaceTypeResponseDTO(surfaceTypeResponseDTO);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/courts/create")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(courtDTO));

        when(courtService.save(any(CourtDTO.class))).thenReturn(courtResponseDTO);

        // Act
        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andReturn();

        String responseBodyAsString = mvcResult.getResponse().getContentAsString();

        CourtResponseDTO responseDTO =
                objectMapper.readValue(responseBodyAsString, CourtResponseDTO.class);

        // Assert
        assertEquals(courtResponseDTO.getId(), responseDTO.getId(),
                "Court id mismatch");

        assertEquals(courtResponseDTO.getCourtNumber(), responseDTO.getCourtNumber(),
                "Court number mismatch");

        assertEquals(surfaceTypeResponseDTO.getId(), responseDTO.getSurfaceTypeResponseDTO().getId(),
                "SurfaceType mismatch");

        assertEquals(201, mvcResult.getResponse().getStatus(),
                "Should return status code 201 CREATED");

        // Verify
        verify(courtService,times(1)).save(any(CourtDTO.class));

    }

    @Test
    @DisplayName("Create court with already existed court number should return 404")
    void createCourt_inValidDataWithAlreadyExistedCourtNumber_return404() throws Exception {

        // Arrange
        int courtNumber = 101;
        Long surfaceTypeId = 1L;
        Long courtId = 2L;

        CourtDTO courtDTO = new CourtDTO();
        courtDTO.setCourtNumber(courtNumber);
        courtDTO.setSurfaceTypeId(surfaceTypeId);


        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/courts/create")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(courtDTO));

        when(courtService.save(any(CourtDTO.class)))
                .thenThrow(new CourtNumberAlreadyExistsException("Court number already exists."));

        // Act
        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andReturn();


        // Assert
        assertEquals(404, mvcResult.getResponse().getStatus(),
                "Should return status code 404 NOT FOUND");

        // Verify
        verify(courtService,times(1)).save(any(CourtDTO.class));

    }

    @Test
    @DisplayName("Create court with non existed surface type should return 404")
    void createCourt_inValidDataWithNonExistedSurfaceType_return404() throws Exception {

        // Arrange
        int courtNumber = 101;
        Long surfaceTypeId = 1L;

        CourtDTO courtDTO = new CourtDTO();
        courtDTO.setCourtNumber(courtNumber);
        courtDTO.setSurfaceTypeId(surfaceTypeId);


        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/courts/create")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(courtDTO));

        when(courtService.save(any(CourtDTO.class)))
                .thenThrow(new SurfaceTypeNotFoundException("Surface type not found."));

        // Act
        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andReturn();


        // Assert
        assertEquals(404, mvcResult.getResponse().getStatus(),
                "Should return status code 404 NOT FOUND");

        // Verify
        verify(courtService,times(1)).save(any(CourtDTO.class));

    }

    @Test
    @DisplayName("Fetch court by valid id")
    void getCourt_validId_returnCourtDetail() throws Exception {
        // Arrange
        int courtNumber = 101;
        Long surfaceTypeId = 1L;
        Long courtId = 2L;

        SurfaceTypeResponseDTO surfaceTypeResponseDTO = new SurfaceTypeResponseDTO();
        surfaceTypeResponseDTO.setId(surfaceTypeId);
        surfaceTypeResponseDTO.setName("Grass");

        CourtResponseDTO courtResponseDTO = new CourtResponseDTO();
        courtResponseDTO.setId(courtId);
        courtResponseDTO.setCourtNumber(courtNumber);
        courtResponseDTO.setSurfaceTypeResponseDTO(surfaceTypeResponseDTO);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/courts/{id}", courtId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        when(courtService.getCourtById(eq(courtId))).thenReturn(courtResponseDTO);

        // Act
        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andReturn();

        String responseBodyAsString = mvcResult.getResponse().getContentAsString();

        CourtResponseDTO responseDTO =
                objectMapper.readValue(responseBodyAsString, CourtResponseDTO.class);

        // Assert
        assertEquals(courtResponseDTO.getId(), responseDTO.getId(),
                "Court id mismatch");

        assertEquals(courtResponseDTO.getCourtNumber(), responseDTO.getCourtNumber(),
                "Court number mismatch");

        assertEquals(surfaceTypeResponseDTO.getId(), responseDTO.getSurfaceTypeResponseDTO().getId(),
                "SurfaceType mismatch");

        assertEquals(200, mvcResult.getResponse().getStatus(),
                "Should return status code 200");

        // Verify
        verify(courtService,times(1)).getCourtById(eq(courtId));

    }

    @Test
    @DisplayName("Fetch court by invalid id")
    void getCourt_inValidId_return404() throws Exception {
        // Arrange
        Long courtId = 999L;

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/courts/{id}", courtId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        when(courtService.getCourtById(eq(courtId)))
                .thenThrow(new CourtNotFoundException("Court not found."));

        // Act
        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andReturn();

        // Assert
        assertEquals(404, mvcResult.getResponse().getStatus(),
                "Should return status code 404 NOT FOUND");

        // Verify
        verify(courtService,times(1)).getCourtById(eq(courtId));


    }

    @Test
    @DisplayName("Fetch all courts")
    void getAllCourts_returnAllExistedCourtsDetail() throws Exception {
        // Arrange
        SurfaceTypeResponseDTO surfaceTypeResponseDTO = new SurfaceTypeResponseDTO();
        surfaceTypeResponseDTO.setId(1L);
        surfaceTypeResponseDTO.setName("Grass");

        CourtResponseDTO courtResponseDTO1 = new CourtResponseDTO();
        courtResponseDTO1.setId(1L);
        courtResponseDTO1.setCourtNumber(101);
        courtResponseDTO1.setSurfaceTypeResponseDTO(surfaceTypeResponseDTO);

        CourtResponseDTO courtResponseDTO2 = new CourtResponseDTO();
        courtResponseDTO2.setId(2L);
        courtResponseDTO2.setCourtNumber(102);
        courtResponseDTO2.setSurfaceTypeResponseDTO(surfaceTypeResponseDTO);

        List<CourtResponseDTO> courtResponseDTOs = List.of(courtResponseDTO1, courtResponseDTO2);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/courts")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        when(courtService.getAllCourts()).thenReturn(courtResponseDTOs);

        // Act
        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andReturn();

        String responseBodyAsString = mvcResult.getResponse().getContentAsString();

        List <CourtResponseDTO> responseDTOs =
                objectMapper.readValue(responseBodyAsString, new TypeReference<List<CourtResponseDTO>>(){
                    });

        // Assert
        assertEquals(courtResponseDTO1.getId(), responseDTOs.get(0).getId(),
                "Court1 id mismatch");

        assertEquals(courtResponseDTO1.getCourtNumber(), responseDTOs.get(0).getCourtNumber(),
                "Court1 number mismatch");

        assertEquals(surfaceTypeResponseDTO.getId(), responseDTOs.get(0).getSurfaceTypeResponseDTO().getId(),
                "SurfaceType mismatch");

        assertEquals(courtResponseDTO2.getId(), responseDTOs.get(1).getId(),
                "Court1 id mismatch");



        assertEquals(courtResponseDTO2.getCourtNumber(), responseDTOs.get(1).getCourtNumber(),
                "Court1 number mismatch");

        assertEquals(surfaceTypeResponseDTO.getId(), responseDTOs.get(1).getSurfaceTypeResponseDTO().getId(),
                "SurfaceType mismatch");

        assertEquals(surfaceTypeResponseDTO.getId(), responseDTOs.get(1).getSurfaceTypeResponseDTO().getId(),
                "SurfaceType mismatch");

        assertEquals(200, mvcResult.getResponse().getStatus(),
                "Should return status code 200 OK");

        // Verify
        verify(courtService,times(1)).getAllCourts();

    }

    @Test
    @DisplayName("Fetch all non existed court should return 404")
    void getAllCourts_NonExistedCourtsInDatabase_return404() throws Exception {

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/courts")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        when(courtService.getAllCourts())
                .thenThrow(new CourtNotFoundException("No Courts found in the database"));

        // Act
        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andReturn();

        // Assert
        assertEquals(404, mvcResult.getResponse().getStatus(),
                "Should return status code 404 NOT FOUND");

        // Verify
        verify(courtService,times(1)).getAllCourts();
    }

    @Test
    @DisplayName("Update court")
    void updateCourt_validCourtNumberAndSurfaceType_returnUpdateCourtDetail() throws Exception {

        // Arrange
        int courtNumber = 101;
        Long surfaceTypeId = 1L;
        Long courtId = 2L;

        CourtDTO courtDTO = new CourtDTO();
        courtDTO.setCourtNumber(courtNumber);
        courtDTO.setSurfaceTypeId(surfaceTypeId);

        SurfaceTypeResponseDTO surfaceTypeResponseDTO = new SurfaceTypeResponseDTO();
        surfaceTypeResponseDTO.setId(surfaceTypeId);
        surfaceTypeResponseDTO.setName("Grass");

        CourtResponseDTO courtResponseDTO = new CourtResponseDTO();
        courtResponseDTO.setId(courtId);
        courtResponseDTO.setCourtNumber(201);
        courtResponseDTO.setSurfaceTypeResponseDTO(surfaceTypeResponseDTO);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/api/courts/{id}", courtId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(courtDTO));

        when(courtService.updateCourt(any(CourtDTO.class), eq(courtId))).thenReturn(courtResponseDTO);

        // Act
        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andReturn();

        String responseBodyAsString = mvcResult.getResponse().getContentAsString();

        CourtResponseDTO responseDTO =
                objectMapper.readValue(responseBodyAsString, CourtResponseDTO.class);

        // Assert
        assertEquals(courtResponseDTO.getId(), responseDTO.getId(),
                "Court id mismatch");

        assertEquals(courtResponseDTO.getCourtNumber(), responseDTO.getCourtNumber(),
                "Court number mismatch");

        assertEquals(surfaceTypeResponseDTO.getId(), responseDTO.getSurfaceTypeResponseDTO().getId(),
                "SurfaceType mismatch");

        assertEquals(200, mvcResult.getResponse().getStatus(),
                "Should return status code 200 OK");

        // Verify
        verify(courtService,times(1)).updateCourt(any(CourtDTO.class), eq(courtId));

    }

    @Test
    @DisplayName("Update non existed surface type should return 404")
    void updateCourt_invalidSurfaceType_return404() throws Exception {

        // Arrange
        int courtNumber = 999;
        Long surfaceTypeId = 1L;
        Long courtId = 2L;

        CourtDTO courtDTO = new CourtDTO();
        courtDTO.setCourtNumber(courtNumber);
        courtDTO.setSurfaceTypeId(surfaceTypeId);

        SurfaceTypeResponseDTO surfaceTypeResponseDTO = new SurfaceTypeResponseDTO();
        surfaceTypeResponseDTO.setId(surfaceTypeId);
        surfaceTypeResponseDTO.setName("Grass");


        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/api/courts/{id}", courtId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(courtDTO));

        when(courtService.updateCourt(any(CourtDTO.class), eq(courtId)))
                .thenThrow(new SurfaceTypeNotFoundException("Surface type was not found in the database"));

        // Act
        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andReturn();

        // Assert
       assertEquals(404, mvcResult.getResponse().getStatus(),
                "Should return status code 404 NOT FOUND");

        // Verify
        verify(courtService,times(1)).updateCourt(any(CourtDTO.class), eq(courtId));

    }

    @Test
    @DisplayName("Update non existed court should return 404")
    void updateCourt_invalidCourtNumber_return404() throws Exception {

        // Arrange
        int courtNumber = 999;
        Long surfaceTypeId = 1L;
        Long courtId = 2L;

        CourtDTO courtDTO = new CourtDTO();
        courtDTO.setCourtNumber(courtNumber);
        courtDTO.setSurfaceTypeId(surfaceTypeId);

        SurfaceTypeResponseDTO surfaceTypeResponseDTO = new SurfaceTypeResponseDTO();
        surfaceTypeResponseDTO.setId(surfaceTypeId);
        surfaceTypeResponseDTO.setName("Grass");


        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/api/courts/{id}", courtId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(courtDTO));

        when(courtService.updateCourt(any(CourtDTO.class), eq(courtId)))
                .thenThrow(new CourtNotFoundException("No Court found in the database"));

        // Act
        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andReturn();

        // Assert
        assertEquals(404, mvcResult.getResponse().getStatus(),
                "Should return status code 404 NOT FOUND");

        // Verify
        verify(courtService,times(1)).updateCourt(any(CourtDTO.class), eq(courtId));

    }

    @Test
    @DisplayName("Delete court")
    void deleteCourt_validId_returnSuccessMessage() throws Exception {
        // Arrange
        Long courtId = 1L;

        Map<String, String> response = Map.of(
                "message", "Court with id " + courtId + " was deleted");

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/api/courts/{id}", courtId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        // Act
        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andReturn();

        String responseBodyAsString = mvcResult.getResponse().getContentAsString();

        Map<String, String> successDeletedResponse =
                objectMapper.readValue(responseBodyAsString, new TypeReference<Map<String, String>>() {
                });

        // Assert
        assertEquals(response.get("message"),successDeletedResponse.get("message"), "Message should be same");
        assertEquals(200, mvcResult.getResponse().getStatus(),"Should return 200 OK");

        // Verify
        verify(courtService,times(1)).deleteCourt(eq(courtId));
    }

    @Test
    @DisplayName("Delete court invalid id should return 404")
    void deleteCourt_inValidId_returnSuccessMessage() throws Exception {
        // Arrange
        Long courtId = 999L;

        Map<String, String> response = Map.of(
                "message", "Court with id " + courtId + " was deleted");

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/api/courts/{id}", courtId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        doThrow( new CourtNotFoundException("No Court found in the database"))
                .when(courtService).deleteCourt(eq(courtId));

        // Act
        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andReturn();

        // Assert
        assertEquals(404, mvcResult.getResponse().getStatus(),
                "Should return 404 NOT FOUND");

        // Verify
        verify(courtService,times(1)).deleteCourt(eq(courtId));
    }





































}