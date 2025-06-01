package cz.vojtechsika.tennisclub.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DataInitializerTest {

    @Mock
    JdbcTemplate jdbc;

    @InjectMocks
    DataInitializer dataInitializer;

    @Test
    @DisplayName("Initialize process is On")
    void run_initDataTrue_runInitializeProcess() throws Exception {

        // Arrange
        dataInitializer.setInitData(true);

        InOrder inOrder = inOrder(jdbc);

        // Act
        dataInitializer.run();

        // Assert
        inOrder.verify(jdbc, times(1))
                .execute("INSERT INTO surface_type (name, minute_price, deleted) VALUES ('Clay', 1.5, false)");
        inOrder.verify(jdbc, times(1))
                .execute("INSERT INTO surface_type (name, minute_price, deleted) VALUES ('Grass', 2.5, false)");

        inOrder.verify(jdbc, times(1)).execute("INSERT INTO court (court_number, surface_type_id, deleted) VALUES (101, 1, false)");
        inOrder.verify(jdbc, times(1)).execute("INSERT INTO court (court_number, surface_type_id, deleted) VALUES (102, 1, false)");
        inOrder.verify(jdbc, times(1)).execute("INSERT INTO court (court_number, surface_type_id, deleted) VALUES (103, 1, false)");
        inOrder.verify(jdbc, times(1)).execute("INSERT INTO court (court_number, surface_type_id, deleted) VALUES (104, 2, false)");

        inOrder.verifyNoMoreInteractions();
    }

    @Test
    @DisplayName("Initialize process is Off")
    void run_initDataFalse_runInitializeProcessIsOff() throws Exception {

        // Arrange
        dataInitializer.setInitData(false);
        InOrder inOrder = inOrder(jdbc);

        // Act
        dataInitializer.run();

        // Assert
        inOrder.verifyNoMoreInteractions();

    }
}