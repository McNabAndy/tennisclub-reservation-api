package cz.vojtechsika.tennisclub.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.vojtechsika.tennisclub.dto.SurfaceTypeDTO;
import cz.vojtechsika.tennisclub.dto.response.SurfaceTypeResponseDTO;
import cz.vojtechsika.tennisclub.exception.SurfaceTypeNotFoundException;
import cz.vojtechsika.tennisclub.service.SurfaceTypeService;
import cz.vojtechsika.tennisclub.service.SurfaceTypeServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@WebMvcTest(SurfaceTypeController.class)
class SurfaceTypeControllerWebLayerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    SurfaceTypeService surfaceTypeService;


    @Autowired
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("Surface type can be created")
    @Order(1)
    void createSurfaceType_validSurfaceTypeDetailsProvided_returnCreatedSurfaceTypeDetail() throws Exception {

        // Arrange
        SurfaceTypeDTO surfaceTypeDTO = new SurfaceTypeDTO();
        surfaceTypeDTO.setName("test");
        surfaceTypeDTO.setMinutePrice(BigDecimal.valueOf(10));

        SurfaceTypeResponseDTO surfaceTypeResponseDTO = new SurfaceTypeResponseDTO();
        surfaceTypeResponseDTO.setId(1L);
        surfaceTypeResponseDTO.setName("test");
        surfaceTypeResponseDTO.setMinutePrice(BigDecimal.valueOf(10));

        when(surfaceTypeService.saveSurfaceType(any(SurfaceTypeDTO.class))).thenReturn(surfaceTypeResponseDTO);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/surfaces/create")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(surfaceTypeDTO));

        // Act
        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andReturn();

        String responseBodyAsString = mvcResult.getResponse().getContentAsString();

        SurfaceTypeResponseDTO createSurfaceTypeResponseDTO =
                objectMapper.readValue(responseBodyAsString, SurfaceTypeResponseDTO.class);

        // Assert
        assertEquals(surfaceTypeResponseDTO.getName(), createSurfaceTypeResponseDTO.getName(),
                "SurfaceType name mismatch" );

        assertEquals(surfaceTypeResponseDTO.getMinutePrice(), createSurfaceTypeResponseDTO.getMinutePrice(),
                "SurfaceType price mismatch" );

        assertEquals(surfaceTypeResponseDTO.getId(), createSurfaceTypeResponseDTO.getId(),
                "SurfaceType id mismatch" );

        assertEquals(201, mvcResult.getResponse().getStatus(),
                "Should return 201 CREATED");

        // Verify
        verify(surfaceTypeService, times(1)).saveSurfaceType(any(SurfaceTypeDTO.class));

    }

    @Test
    @DisplayName("Fetch surface type by valid id")
    @Order(2)
    void getSurfaceType_fetchSurfaceTypeByValidId_returnDetailOfExistingSurfaceType() throws Exception {

        // Arrange
        Long surfaceTypeId = 1L;

        SurfaceTypeResponseDTO surfaceTypeResponseDTO = new SurfaceTypeResponseDTO();
        surfaceTypeResponseDTO.setId(surfaceTypeId);
        surfaceTypeResponseDTO.setName("test");
        surfaceTypeResponseDTO.setMinutePrice(BigDecimal.valueOf(10));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/surfaces/{id}", surfaceTypeId)
                .accept(MediaType.APPLICATION_JSON);

        when(surfaceTypeService.getSurfaceTypeById(surfaceTypeId)).thenReturn(surfaceTypeResponseDTO);

        // Act
        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andReturn();

        String responseBodyAsString = mvcResult.getResponse().getContentAsString();

        SurfaceTypeResponseDTO responseDTO =
                objectMapper.readValue(responseBodyAsString, SurfaceTypeResponseDTO.class);


        // Assert
        assertEquals(surfaceTypeResponseDTO.getId(), responseDTO.getId(),
                "SurfaceType id mismatch");

        assertEquals(surfaceTypeResponseDTO.getName(), responseDTO.getName(),
                "SurfaceType name mismatch");

        assertEquals(surfaceTypeResponseDTO.getMinutePrice(), responseDTO.getMinutePrice(),
                "SurfaceType price mismatch");

        assertEquals(200, mvcResult.getResponse().getStatus(),
                "Should return 200 OK");
        // verify
        verify(surfaceTypeService, times(1)).getSurfaceTypeById(surfaceTypeId);

    }

    @Test
    @DisplayName("Fetch surface type by invalid id should return 404")
    @Order(3)
    void getSurfaceType_fetchSurfaceTypeByInvalidId_return404() throws Exception {

        // Arrange
        Long nonExistedSurfaceTypeId = 999L;


        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/surfaces/{id}", nonExistedSurfaceTypeId)
                .accept(MediaType.APPLICATION_JSON);

        when(surfaceTypeService.getSurfaceTypeById(nonExistedSurfaceTypeId))
                .thenThrow(new SurfaceTypeNotFoundException("Surface type not found"));

        // Act
        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andReturn();

        // Assert
        assertEquals( 404, mvcResult.getResponse().getStatus(),
                "Should return 404 status code");

    }

    @Test
    @DisplayName("Fetch All existing surface type")
    @Order(4)
    void getAllSurfaceTypes_fetchAllSurfaceType_returnListOfSurfaceTypesDetails() throws Exception {

        SurfaceTypeResponseDTO surfaceTypeResponseDTO1 = new SurfaceTypeResponseDTO();
        surfaceTypeResponseDTO1.setId(1L);
        surfaceTypeResponseDTO1.setName("Grass");
        surfaceTypeResponseDTO1.setMinutePrice(BigDecimal.valueOf(10));

        SurfaceTypeResponseDTO surfaceTypeResponseDTO2 = new SurfaceTypeResponseDTO();
        surfaceTypeResponseDTO2.setId(2L);
        surfaceTypeResponseDTO2.setName("Clay");
        surfaceTypeResponseDTO2.setMinutePrice(BigDecimal.valueOf(20));

        List<SurfaceTypeResponseDTO> surfaceTypeResponseDTOs = List.of(surfaceTypeResponseDTO1, surfaceTypeResponseDTO2);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/surfaces")
                .accept(MediaType.APPLICATION_JSON);

        when(surfaceTypeService.getAllSurfaceTypes()).thenReturn(surfaceTypeResponseDTOs);

        // Act
        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andReturn();

        String responseBodyAsString = mvcResult.getResponse().getContentAsString();

        List<SurfaceTypeResponseDTO> responseDTOs =
                objectMapper.readValue(responseBodyAsString, new TypeReference<List<SurfaceTypeResponseDTO>>() {
                });


        // Assert
        assertEquals(2, responseDTOs.size(),
                "Expected 2 surface types");

        assertEquals(surfaceTypeResponseDTO1.getId(), responseDTOs.get(0).getId(),
                "SurfaceType1 id mismatch");
        assertEquals(surfaceTypeResponseDTO1.getName(), responseDTOs.get(0).getName(),
                "SurfaceType1 name mismatch");
        assertEquals(surfaceTypeResponseDTO1.getMinutePrice(), responseDTOs.get(0).getMinutePrice(),
                "SurfaceType1 minute price mismatch");

        assertEquals(surfaceTypeResponseDTO2.getId(), responseDTOs.get(1).getId(),
                "SurfaceType2 name mismatch");
        assertEquals(surfaceTypeResponseDTO2.getName(), responseDTOs.get(1).getName(),
                "SurfaceType1 name mismatch");
        assertEquals(surfaceTypeResponseDTO2.getMinutePrice(), responseDTOs.get(1).getMinutePrice(),
                "SurfaceType1 minute price mismatch");

        assertEquals(200, mvcResult.getResponse().getStatus(),
                "Should return 200 OK");

        // verify
        verify(surfaceTypeService, times(1)).getAllSurfaceTypes();





    }

    @Test
    @DisplayName("Fetch All non existed surface type should return 404")
    @Order(5)
    void getAllSurfaceTypes_fetchAllSurfaceTypes_return404() throws Exception {

        // Arrange
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/surfaces")
                .accept(MediaType.APPLICATION_JSON);

        when(surfaceTypeService.getAllSurfaceTypes()).thenThrow(
                new SurfaceTypeNotFoundException("Surface type not found"));

        // Act
        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andReturn();

        // Assert
        assertEquals( 404, mvcResult.getResponse().getStatus(),
                "Should return 404 status code");

    }

    @Test
    @DisplayName("Update surface type")
    @Order(6)
    void updateSurfaceType_validDTOWithValidId_returnUpdateSurfaceTypeDetail() throws Exception {
        // Arrange
        Long surfaceTypeId = 1L;

        SurfaceTypeDTO surfaceTypeDTO = new SurfaceTypeDTO();
        surfaceTypeDTO.setName("test");
        surfaceTypeDTO.setMinutePrice(BigDecimal.valueOf(10));

        SurfaceTypeResponseDTO surfaceTypeResponseDTO = new SurfaceTypeResponseDTO();
        surfaceTypeResponseDTO.setId(surfaceTypeId);
        surfaceTypeResponseDTO.setName("test");
        surfaceTypeResponseDTO.setMinutePrice(BigDecimal.valueOf(10));

        when(surfaceTypeService.updateSurfaceType(any(SurfaceTypeDTO.class), eq(surfaceTypeId)))
                .thenReturn(surfaceTypeResponseDTO);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/api/surfaces/{id}", surfaceTypeId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(surfaceTypeDTO));

        // Act
        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andReturn();

        String responseBodyAsString = mvcResult.getResponse().getContentAsString();

        SurfaceTypeResponseDTO createSurfaceTypeResponseDTO =
                objectMapper.readValue(responseBodyAsString, SurfaceTypeResponseDTO.class);

        // Assert
        assertEquals(surfaceTypeResponseDTO.getName(), createSurfaceTypeResponseDTO.getName(),
                "SurfaceType name mismatch");

        assertEquals(surfaceTypeResponseDTO.getMinutePrice(), createSurfaceTypeResponseDTO.getMinutePrice(),
                "SurfaceType price mismatch");

        assertEquals(surfaceTypeResponseDTO.getId(), createSurfaceTypeResponseDTO.getId(),
                "SurfaceType id mismatch");

        assertEquals(200, mvcResult.getResponse().getStatus(),
                "Should return 200 OK");

        // Verify
        verify(surfaceTypeService, times(1))
                .updateSurfaceType(any(SurfaceTypeDTO.class), eq(surfaceTypeId));
    }

    @Test
    @DisplayName("Update surface type with invalid id should return 404")
    @Order(7)
    void updateSurfaceType_validDTOWithInValidId_return404() throws Exception {
        // Arrange
        Long surfaceTypeId = 1L;

        SurfaceTypeDTO surfaceTypeDTO = new SurfaceTypeDTO();
        surfaceTypeDTO.setName("test");
        surfaceTypeDTO.setMinutePrice(BigDecimal.valueOf(10));

        SurfaceTypeResponseDTO surfaceTypeResponseDTO = new SurfaceTypeResponseDTO();
        surfaceTypeResponseDTO.setId(surfaceTypeId);
        surfaceTypeResponseDTO.setName("test");
        surfaceTypeResponseDTO.setMinutePrice(BigDecimal.valueOf(10));

        when(surfaceTypeService.updateSurfaceType(any(SurfaceTypeDTO.class), eq(surfaceTypeId)))
                .thenThrow(new SurfaceTypeNotFoundException("Surface type not found"));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/api/surfaces/{id}", surfaceTypeId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(surfaceTypeDTO));

        // Act
        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andReturn();

        // Assert
        assertEquals( 404, mvcResult.getResponse().getStatus(),
                "Should return 404 status code");

        // Verify
        verify(surfaceTypeService, times(1))
                .updateSurfaceType(any(SurfaceTypeDTO.class), eq(surfaceTypeId));
    }

    @Test
    @DisplayName("Delete surface type")
    @Order(8)
    void deleteSurfaceType_validId_returnSuccessMessage() throws Exception {

        // Arrange
        Long surfaceTypeId = 1L;

        Map<String, String> response = Map.of(
                "message", "Surface Type with ID " + surfaceTypeId + " was deleted.");

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/api/surfaces/{id}", surfaceTypeId)
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
        verify(surfaceTypeService, times(1)).deleteSurfaceType(eq(surfaceTypeId));
    }

    @Test
    @DisplayName("Delete surface type by invalid id should return 404")
    @Order(9)
    void deleteSurfaceType_inValidId_return404() throws Exception {

        // Arrange
        Long surfaceTypeId = 1L;

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/api/surfaces/{id}", surfaceTypeId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        doThrow(new SurfaceTypeNotFoundException("Surface type not found"))
                .when(surfaceTypeService).deleteSurfaceType(eq(surfaceTypeId));

        // Act
        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andReturn();

        // Assert
        assertEquals( 404, mvcResult.getResponse().getStatus(),
                "Should return 404 status code");

        // Verify
        verify(surfaceTypeService, times(1)).deleteSurfaceType(eq(surfaceTypeId));
    }






}