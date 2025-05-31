package cz.vojtechsika.tennisclub.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.vojtechsika.tennisclub.dto.ReservationDTO;
import cz.vojtechsika.tennisclub.dto.response.ReservationResponseDTO;
import cz.vojtechsika.tennisclub.dto.response.SurfaceTypeResponseDTO;
import cz.vojtechsika.tennisclub.exception.CourtNotFoundException;
import cz.vojtechsika.tennisclub.exception.ReservationNotFoundException;
import cz.vojtechsika.tennisclub.exception.ReservationValidationException;
import cz.vojtechsika.tennisclub.service.ReservationService;
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

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@WebMvcTest(controllers = ReservationController.class)
class ReservationControllerWebLayerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    ReservationService reservationService;

    @Autowired
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("Fetch reservation by valid id")
    @Order(1)
    void getReservation_validId_returnReservationDetail() throws Exception {

        // Arrange
        Long reservationId = 1L;
        int courtNumber = 101;

        ReservationResponseDTO reservationResponseDTO = new ReservationResponseDTO();
        reservationResponseDTO.setId(reservationId);
        reservationResponseDTO.setPrice(BigDecimal.valueOf(100));
        reservationResponseDTO.setCourtNumber(courtNumber);


        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/reservations/{id}", reservationId)
                .accept(MediaType.APPLICATION_JSON);

        when(reservationService.getReservationById(eq(reservationId))).thenReturn(reservationResponseDTO);

        // Act
        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andReturn();

        String responseBodyAsString = mvcResult.getResponse().getContentAsString();

        ReservationResponseDTO responseDTO =
                objectMapper.readValue(responseBodyAsString, ReservationResponseDTO.class);


        // Assert
        assertEquals(reservationResponseDTO.getId(), responseDTO.getId(),
                "Reservation id mismatch");

        assertEquals(reservationResponseDTO.getPrice(), responseDTO.getPrice(),
                "Reservation price mismatch");

        assertEquals(reservationResponseDTO.getCourtNumber(), responseDTO.getCourtNumber(),
                "Reservation number mismatch");

        assertEquals(200, mvcResult.getResponse().getStatus(),
                "Should return 200 OK");
        // verify
        verify(reservationService, times(1)).getReservationById(eq(reservationId));

    }

    @Test
    @DisplayName("Fetch reservation by invalid id")
    @Order(2)
    void getReservation_fetchReservationByInValidId_return404() throws Exception {

        // Arrange
        Long nonExistedReservationId = 999L;


        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/reservations/{id}", nonExistedReservationId)
                .accept(MediaType.APPLICATION_JSON);

        when(reservationService.getReservationById(eq(nonExistedReservationId)))
                .thenThrow(new ReservationNotFoundException("Reservation not found"));

        // Act
        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andReturn();

        // Assert
        assertEquals( 404, mvcResult.getResponse().getStatus(),
                "Should return 404 status code");

        // verify
        verify(reservationService, times(1)).getReservationById(eq(nonExistedReservationId));

    }

    @Test
    @DisplayName("Fetch all reservation by valid court number")
    @Order(3)
    void getReservationByCourtNumber_fetchAllReservationByCourtNumber_returnListOfReservationDetails() throws Exception {

        // Arrange
        int courtNumber = 101;

        ReservationResponseDTO reservationResponseDTO1 = new ReservationResponseDTO();
        reservationResponseDTO1.setId(1L);
        reservationResponseDTO1.setPrice(BigDecimal.valueOf(10));
        reservationResponseDTO1.setCourtNumber(courtNumber);

        ReservationResponseDTO reservationResponseDTO2 = new ReservationResponseDTO();
        reservationResponseDTO2.setId(2L);
        reservationResponseDTO2.setPrice(BigDecimal.valueOf(20));
        reservationResponseDTO2.setCourtNumber(courtNumber);

        List<ReservationResponseDTO> reservationResponseDTOs = List.of(reservationResponseDTO1, reservationResponseDTO2);


        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/reservations/court/{id}", courtNumber)
                .accept(MediaType.APPLICATION_JSON);

        when(reservationService.
                getReservationByCourtNumber(eq(courtNumber))).thenReturn(reservationResponseDTOs);

        // Act
        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andReturn();

        String responseBodyAsString = mvcResult.getResponse().getContentAsString();

        List <ReservationResponseDTO> responseDTOs =
                objectMapper.readValue(responseBodyAsString, new TypeReference<List<ReservationResponseDTO>>() {
                });

        // Assert
        assertEquals(2, responseDTOs.size(),
                "Expected 2 reservation");

        assertEquals(responseDTOs.get(0).getCourtNumber(), responseDTOs.get(1).getCourtNumber(),
                "Court number mismatch");


        assertEquals(reservationResponseDTO1.getId(), responseDTOs.get(0).getId(),
                "Reservation1 id mismatch");
        assertEquals(reservationResponseDTO1.getCourtNumber(), responseDTOs.get(0).getCourtNumber(),
                "Reservation1 court number mismatch");
        assertEquals(reservationResponseDTO1.getPrice(), responseDTOs.get(0).getPrice(),
                "Reservation1  price mismatch");


        assertEquals(reservationResponseDTO2.getId(), responseDTOs.get(1).getId(),
                "Reservation2 id mismatch");
        assertEquals(reservationResponseDTO2.getCourtNumber(), responseDTOs.get(1).getCourtNumber(),
                "Reservation2 court number mismatch");
        assertEquals(reservationResponseDTO2.getPrice(), responseDTOs.get(1).getPrice(),
                "Reservation2  price mismatch");


        assertEquals(200, mvcResult.getResponse().getStatus(),
                "Should return 200 OK");

        // verify
        verify(reservationService, times(1)).getReservationByCourtNumber(eq(courtNumber));



    }

    @Test
    @DisplayName("Fetch all reservation by invalid court number should return 404")
    @Order(4)
    void getReservationByCourtNumber_fetchAllReservationByNonExistCourtNumber_return404() throws Exception {

        // Arrange
        int courtNumber = 999;

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/reservations/court/{id}", courtNumber)
                .accept(MediaType.APPLICATION_JSON);

        when(reservationService.
                getReservationByCourtNumber(eq(courtNumber)))
                .thenThrow(new ReservationNotFoundException("Reservation not found"));

        // Act
        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andReturn();


        // Assert
        assertEquals(404, mvcResult.getResponse().getStatus(),
                "Should return 404 status code");

        // verify
        verify(reservationService, times(1)).getReservationByCourtNumber(eq(courtNumber));



    }

    @Test
    @DisplayName("Fetch all future reservation by valid phone number")
    void getReservationByPhoneNumber_fetchAllFutureReservationByValidPhoneNumber_returnListOfReservationDetails() throws Exception {

        // Arrange
        int courtNumber = 101;
        String phoneNumber = "123456789";
        boolean futureOnly = true;

        ReservationResponseDTO reservationResponseDTO1 = new ReservationResponseDTO();
        reservationResponseDTO1.setId(1L);
        reservationResponseDTO1.setPrice(BigDecimal.valueOf(10));
        reservationResponseDTO1.setCourtNumber(courtNumber);
        reservationResponseDTO1.setPhoneNumber(phoneNumber);

        ReservationResponseDTO reservationResponseDTO2 = new ReservationResponseDTO();
        reservationResponseDTO2.setId(2L);
        reservationResponseDTO2.setPrice(BigDecimal.valueOf(20));
        reservationResponseDTO2.setCourtNumber(courtNumber);
        reservationResponseDTO2.setPhoneNumber(phoneNumber);

        List<ReservationResponseDTO> reservationResponseDTOs = List.of(reservationResponseDTO1, reservationResponseDTO2);


        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/reservations/phone/{phoneNumber}", phoneNumber)
                .queryParam("futureOnly", String.valueOf(futureOnly))
                .accept(MediaType.APPLICATION_JSON);

        when(reservationService.
                getReservationByPhoneNumber(eq(phoneNumber), eq(futureOnly))).thenReturn(reservationResponseDTOs);

        // Act
        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andReturn();

        String responseBodyAsString = mvcResult.getResponse().getContentAsString();

        List <ReservationResponseDTO> responseDTOs =
                objectMapper.readValue(responseBodyAsString, new TypeReference<List<ReservationResponseDTO>>() {
                });

        // Assert
        assertEquals(2, responseDTOs.size(),
                "Expected 2 reservation types");

        assertEquals(responseDTOs.get(0).getPhoneNumber(), responseDTOs.get(1).getPhoneNumber(),
                "Phonenumber mismatch");


        assertEquals(reservationResponseDTO1.getId(), responseDTOs.get(0).getId(),
                "Reservation1 id mismatch");
        assertEquals(reservationResponseDTO1.getCourtNumber(), responseDTOs.get(0).getCourtNumber(),
                "Reservation1 court number mismatch");
        assertEquals(reservationResponseDTO1.getPrice(), responseDTOs.get(0).getPrice(),
                "Reservation1  price mismatch");


        assertEquals(reservationResponseDTO2.getId(), responseDTOs.get(1).getId(),
                "Reservation2 id mismatch");
        assertEquals(reservationResponseDTO2.getCourtNumber(), responseDTOs.get(1).getCourtNumber(),
                "Reservation2 court number mismatch");
        assertEquals(reservationResponseDTO2.getPrice(), responseDTOs.get(1).getPrice(),
                "Reservation2  price mismatch");


        assertEquals(200, mvcResult.getResponse().getStatus(),
                "Should return 200 OK");

        // verify
        verify(reservationService, times(1))
                .getReservationByPhoneNumber(eq(phoneNumber), eq(futureOnly));
    }

    @Test
    @DisplayName("Fetch all past reservation by valid phone number")
    void getReservationByPhoneNumber_fetchAllPastReservationByValidPhoneNumber_returnListOfReservationDetails() throws Exception {

        // Arrange
        int courtNumber = 101;
        String phoneNumber = "123456789";

        ReservationResponseDTO reservationResponseDTO1 = new ReservationResponseDTO();
        reservationResponseDTO1.setId(1L);
        reservationResponseDTO1.setPrice(BigDecimal.valueOf(10));
        reservationResponseDTO1.setCourtNumber(courtNumber);
        reservationResponseDTO1.setPhoneNumber(phoneNumber);

        ReservationResponseDTO reservationResponseDTO2 = new ReservationResponseDTO();
        reservationResponseDTO2.setId(2L);
        reservationResponseDTO2.setPrice(BigDecimal.valueOf(20));
        reservationResponseDTO2.setCourtNumber(courtNumber);
        reservationResponseDTO2.setPhoneNumber(phoneNumber);

        List<ReservationResponseDTO> reservationResponseDTOs = List.of(reservationResponseDTO1, reservationResponseDTO2);


        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/reservations/phone/{phoneNumber}", phoneNumber)
                .accept(MediaType.APPLICATION_JSON);

        when(reservationService.
                getReservationByPhoneNumber(eq(phoneNumber), eq(false))).thenReturn(reservationResponseDTOs);

        // Act
        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andReturn();

        String responseBodyAsString = mvcResult.getResponse().getContentAsString();

        List <ReservationResponseDTO> responseDTOs =
                objectMapper.readValue(responseBodyAsString, new TypeReference<List<ReservationResponseDTO>>() {
                });

        // Assert
        assertEquals(2, responseDTOs.size(),
                "Expected 2 reservation types");

        assertEquals(responseDTOs.get(0).getPhoneNumber(), responseDTOs.get(1).getPhoneNumber(),
                "Phonenumber mismatch");


        assertEquals(reservationResponseDTO1.getId(), responseDTOs.get(0).getId(),
                "Reservation1 id mismatch");
        assertEquals(reservationResponseDTO1.getCourtNumber(), responseDTOs.get(0).getCourtNumber(),
                "Reservation1 court number mismatch");
        assertEquals(reservationResponseDTO1.getPrice(), responseDTOs.get(0).getPrice(),
                "Reservation1  price mismatch");


        assertEquals(reservationResponseDTO2.getId(), responseDTOs.get(1).getId(),
                "Reservation2 id mismatch");
        assertEquals(reservationResponseDTO2.getCourtNumber(), responseDTOs.get(1).getCourtNumber(),
                "Reservation2 court number mismatch");
        assertEquals(reservationResponseDTO2.getPrice(), responseDTOs.get(1).getPrice(),
                "Reservation2  price mismatch");


        assertEquals(200, mvcResult.getResponse().getStatus(),
                "Should return 200 OK");

        // verify
        verify(reservationService, times(1))
                .getReservationByPhoneNumber(eq(phoneNumber), eq(false));
    }

    @Test
    @DisplayName("Fetch all future reservation by invalid phone number should return 404")
    void getReservationByPhoneNumber_fetchAllFutureReservationByInValidPhoneNumber_retur404() throws Exception {

        // Arrange
        String nonExistedPhoneNumber = "123456789";
        boolean futureOnly = true;


        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/reservations/phone/{phoneNumber}", nonExistedPhoneNumber)
                .queryParam("futureOnly", String.valueOf(futureOnly))
                .accept(MediaType.APPLICATION_JSON);

        when(reservationService.
                getReservationByPhoneNumber(eq(nonExistedPhoneNumber), eq(futureOnly)))
                .thenThrow(new ReservationNotFoundException("Reservation not found"));

        // Act
        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andReturn();


        // Assert
       assertEquals(404, mvcResult.getResponse().getStatus(),
                "Should return 404 status code");

        // verify
        verify(reservationService, times(1))
                .getReservationByPhoneNumber(eq(nonExistedPhoneNumber), eq(futureOnly));
    }

    @Test
    @DisplayName("Fetch all past reservation by invalid phone number should return 404")
    void getReservationByPhoneNumber_fetchAllPastReservationByInValidPhoneNumber_return404() throws Exception {

        // Arrange
        String nonExistedPhoneNumber = "123456789";



        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/reservations/phone/{phoneNumber}", nonExistedPhoneNumber)
                .accept(MediaType.APPLICATION_JSON);

        when(reservationService.
                getReservationByPhoneNumber(eq(nonExistedPhoneNumber), eq(false)))
                .thenThrow(new ReservationNotFoundException("Reservation not found"));

        // Act
        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andReturn();


        // Assert
        assertEquals(404, mvcResult.getResponse().getStatus(),
                "Should return 404 status code");

        // verify
        verify(reservationService, times(1))
                .getReservationByPhoneNumber(eq(nonExistedPhoneNumber), eq(false));
    }

    @Test
    @DisplayName("Fetch all reservations")
    void getAllReservations_FetchAllReservation_returnListOfAllReservationsDetails() throws Exception {
        // Arrange

        ReservationResponseDTO reservationResponseDTO1 = new ReservationResponseDTO();
        reservationResponseDTO1.setId(1L);
        reservationResponseDTO1.setPrice(BigDecimal.valueOf(10));
        reservationResponseDTO1.setCourtNumber(101);
        reservationResponseDTO1.setPhoneNumber("111222333");

        ReservationResponseDTO reservationResponseDTO2 = new ReservationResponseDTO();
        reservationResponseDTO2.setId(2L);
        reservationResponseDTO2.setPrice(BigDecimal.valueOf(20));
        reservationResponseDTO2.setCourtNumber(102);
        reservationResponseDTO2.setPhoneNumber("444555666");

        List<ReservationResponseDTO> reservationResponseDTOs = List.of(reservationResponseDTO1, reservationResponseDTO2);


        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/reservations")
                .accept(MediaType.APPLICATION_JSON);

        when(reservationService.
                getAllReservations()).thenReturn(reservationResponseDTOs);

        // Act
        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andReturn();

        String responseBodyAsString = mvcResult.getResponse().getContentAsString();

        List <ReservationResponseDTO> responseDTOs =
                objectMapper.readValue(responseBodyAsString, new TypeReference<List<ReservationResponseDTO>>() {
                });

        // Assert
        assertEquals(2, responseDTOs.size(),
                "Expected 2 reservation types");


        assertEquals(reservationResponseDTO1.getId(), responseDTOs.get(0).getId(),
                "Reservation1 id mismatch");
        assertEquals(reservationResponseDTO1.getCourtNumber(), responseDTOs.get(0).getCourtNumber(),
                "Reservation1 court number mismatch");
        assertEquals(reservationResponseDTO1.getPrice(), responseDTOs.get(0).getPrice(),
                "Reservation1  price mismatch");


        assertEquals(reservationResponseDTO2.getId(), responseDTOs.get(1).getId(),
                "Reservation2 id mismatch");
        assertEquals(reservationResponseDTO2.getCourtNumber(), responseDTOs.get(1).getCourtNumber(),
                "Reservation2 court number mismatch");
        assertEquals(reservationResponseDTO2.getPrice(), responseDTOs.get(1).getPrice(),
                "Reservation2  price mismatch");


        assertEquals(200, mvcResult.getResponse().getStatus(),
                "Should return 200 OK");

    }

    @Test
    @DisplayName("Fetch all non existed reservations should return 404")
    void getAllReservations_FetchAllNonExistReservation_return404() throws Exception {

        // Arrange
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/reservations")
                .accept(MediaType.APPLICATION_JSON);

        when(reservationService.
                getAllReservations()).thenThrow(new ReservationNotFoundException("Reservation not found"));

        // Act
        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andReturn();

        // Assert
        assertEquals(404, mvcResult.getResponse().getStatus(),
                "Should return 404 status code");

    }

    @Test
    @DisplayName("Delete Reservation")
    void deleteReservation_validId_returnSuccessMessage() throws Exception {

        // Arrange
        Long reservationId = 1L;

        Map<String, String> response = Map.of(
                "message", "Reservation with id " + reservationId + " was deleted");

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/api/reservations/{id}", reservationId)
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
        verify(reservationService, times(1)).deleteReservation(eq(reservationId));
    }

    @Test
    @DisplayName("Delete Reservation")
    void deleteReservation_inValidId_return404() throws Exception {

        // Arrange
        Long reservationId = 999L;

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/api/reservations/{id}", reservationId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        doThrow(new ReservationNotFoundException("Reservation not found"))
                .when(reservationService).deleteReservation(eq(reservationId));

        // Act
        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andReturn();

        // Assert
        assertEquals(404, mvcResult.getResponse().getStatus(),"Should return 404 OK");

        // Verify
        verify(reservationService, times(1)).deleteReservation(eq(reservationId));
    }

    @Test
    @DisplayName("Create new reservation")
    void createReservation_validData_returnNewReservationDetail() throws Exception {

        // Arrange
        Long reservationId = 1L;
        int courtNumber = 101;

        ReservationDTO reservationDTO = new ReservationDTO();
        reservationDTO.setUserName("John");
        reservationDTO.setCourtNumber(courtNumber);


        ReservationResponseDTO reservationResponseDTO = new ReservationResponseDTO();
        reservationResponseDTO.setId(reservationId);
        reservationResponseDTO.setUserName("John");
        reservationResponseDTO.setCourtNumber(courtNumber);


        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/reservations/create")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reservationDTO));

        when(reservationService.createReservation(any(ReservationDTO.class))).thenReturn(reservationResponseDTO);

        // Act
        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andReturn();

        String responseBodyAsString = mvcResult.getResponse().getContentAsString();

        ReservationResponseDTO responseDTO =
                objectMapper.readValue(responseBodyAsString, ReservationResponseDTO.class);


        // Assert
        assertEquals(reservationResponseDTO.getId(), responseDTO.getId(),
                "Reservation id mismatch");

        assertEquals(reservationResponseDTO.getUserName(), responseDTO.getUserName(),
                "Reservation user name mismatch");

        assertEquals(reservationResponseDTO.getCourtNumber(), responseDTO.getCourtNumber(),
                "Reservation court number mismatch");

        assertEquals(201, mvcResult.getResponse().getStatus(),
                "Should return status code 201 CREATED");
        // verify
        verify(reservationService, times(1)).createReservation(any(ReservationDTO.class));
    }

    @Test
    @DisplayName("Create new reservation with invalid court number")
    void createReservation_invalidCourtNumber_return404() throws Exception {

        // Arrange
        int courtNumber = 101;

        ReservationDTO reservationDTO = new ReservationDTO();
        reservationDTO.setUserName("John");
        reservationDTO.setCourtNumber(courtNumber);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/reservations/create")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reservationDTO));

        when(reservationService.createReservation(any(ReservationDTO.class)))
                .thenThrow(new CourtNotFoundException("Court not found"));

        // Act
        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andReturn();

        // Assert
        assertEquals(404, mvcResult.getResponse().getStatus(),
                "Should return status code 404");
        // verify
        verify(reservationService, times(1)).createReservation(any(ReservationDTO.class));
    }

    @Test
    @DisplayName("Create new reservation with invalid reservation time should return 404")
    void createReservation_withInValidReservationDateTime_return404() throws Exception {

        // Arrange
        int courtNumber = 101;

        ReservationDTO reservationDTO = new ReservationDTO();
        reservationDTO.setUserName("John");
        reservationDTO.setCourtNumber(courtNumber);


        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/reservations/create")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reservationDTO));

        when(reservationService.createReservation(any(ReservationDTO.class)))
                .thenThrow(new ReservationValidationException("Wrong reservation time or date"));

        // Act
        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andReturn();


        // Assert
        assertEquals(404, mvcResult.getResponse().getStatus(),
                "Should return status code 404");
        // verify
        verify(reservationService, times(1)).createReservation(any(ReservationDTO.class));
    }

    @Test
    @DisplayName("Update reservation")
    void updateReservation_validData_returnUpdatedReservationDetail() throws Exception {
        // Arrange
        Long reservationId = 1L;
        int courtNumber = 101;

        ReservationDTO reservationDTO = new ReservationDTO();
        reservationDTO.setUserName("John");
        reservationDTO.setCourtNumber(courtNumber);


        ReservationResponseDTO reservationResponseDTO = new ReservationResponseDTO();
        reservationResponseDTO.setId(reservationId);
        reservationResponseDTO.setUserName("Tomas");
        reservationResponseDTO.setCourtNumber(courtNumber);


        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/api/reservations/{id}",reservationId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reservationDTO));

        when(reservationService.updateReservation(any(ReservationDTO.class), eq(reservationId))).thenReturn(reservationResponseDTO);

        // Act
        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andReturn();

        String responseBodyAsString = mvcResult.getResponse().getContentAsString();

        ReservationResponseDTO responseDTO =
                objectMapper.readValue(responseBodyAsString, ReservationResponseDTO.class);


        // Assert
        assertEquals(reservationResponseDTO.getId(), responseDTO.getId(),
                "Reservation id mismatch");

        assertEquals(reservationResponseDTO.getUserName(), responseDTO.getUserName(),
                "Reservation user name mismatch");

        assertEquals(reservationResponseDTO.getCourtNumber(), responseDTO.getCourtNumber(),
                "Reservation court number mismatch");

        assertEquals(200, mvcResult.getResponse().getStatus(),
                "Should return status code 200 OK");
        // verify
        verify(reservationService, times(1))
                .updateReservation(any(ReservationDTO.class), eq(reservationId));

    }

    @Test
    @DisplayName("Update reservation with invalid court number")
    void updateReservation_inValidCourtNumber_return404() throws Exception {
        // Arrange
        Long reservationId = 1L;
        int courtNumber = 101;

        ReservationDTO reservationDTO = new ReservationDTO();
        reservationDTO.setUserName("John");
        reservationDTO.setCourtNumber(courtNumber);



        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/api/reservations/{id}",reservationId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reservationDTO));

        when(reservationService.updateReservation(any(ReservationDTO.class), eq(reservationId)))
                .thenThrow(new CourtNotFoundException("Court not found"));

        // Act
        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andReturn();


        // Assert
        assertEquals(404, mvcResult.getResponse().getStatus(),
                "Should return status code 404");
        // verify
        verify(reservationService, times(1))
                .updateReservation(any(ReservationDTO.class), eq(reservationId));

    }

    @Test
    @DisplayName("Update reservation with invalid reservation time should return 404")
    void updateReservation_withInValidReservationDateTime_return404() throws Exception {
        // Arrange
        Long reservationId = 1L;
        int courtNumber = 101;

        ReservationDTO reservationDTO = new ReservationDTO();
        reservationDTO.setUserName("John");
        reservationDTO.setCourtNumber(courtNumber);



        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/api/reservations/{id}",reservationId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reservationDTO));

        when(reservationService.updateReservation(any(ReservationDTO.class), eq(reservationId)))
                .thenThrow(new ReservationValidationException("Wrong reservation date or time"));

        // Act
        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andReturn();


        // Assert
        assertEquals(404, mvcResult.getResponse().getStatus(),
                "Should return status code 404");
        // verify
        verify(reservationService, times(1))
                .updateReservation(any(ReservationDTO.class), eq(reservationId));

    }
}