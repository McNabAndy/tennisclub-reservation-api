package cz.vojtechsika.tennisclub.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;


/**
 * DataInitializer is a Spring Boot component that initializes data in the database upon application startup.
 * It implements the {@link CommandLineRunner} interface, which allows the execution of custom code after the application context is loaded.
 * The class inserts predefined data for surface types and courts into the database based on the value of the {@code app.init.data} property.
 * The class is responsible for populating the database with initial data when the {@code app.init.data} property is set to {@code true}.
 * It ensures that the database contains basic data for surface types and courts, which can be useful for testing or starting the application with some initial state.</p>
 */
@Component
@Getter
@Setter
public class DataInitializer implements CommandLineRunner {

    /**
     * Spring component for interact with the database
     */
    private JdbcTemplate jdbc;


    /**
     * A flag that indicates whether the initialization of data should be performed.
     * This value is injected from the application properties.
     */
    @Value("${app.init.data}")
    private boolean initData;


    /**
     * Constructs a new DataInitializer with the provided {@link JdbcTemplate}.
     *
     * @param jdbc The {@link JdbcTemplate} used to interact with the database.
     */
    @Autowired
    public DataInitializer(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    /**
     * Executes the data initialization process if the {@code app.init.data} property is set to {@code true}.
     * The method inserts initial data for surface types and courts into the database.
     *
     * If the {@code initData} flag is {@code true}, the following data is inserted:
     *
     * Surface Types: "Clay" and "Grass" with associated prices.
     * Courts: Four courts with numbers 101, 102, 103, and 104, each associated with a surface type.
     *
     *
     * @param args Command-line arguments (not used in this implementation).
     * @throws Exception If any error occurs during data initialization.
     */
    @Override
    public void run(String... args) throws Exception {

        if (initData) {
            // Initialize Surface Types
            jdbc.execute("INSERT INTO surface_type (name, minute_price, deleted) VALUES ('Clay', 1.5, false)");
            jdbc.execute("INSERT INTO surface_type (name, minute_price, deleted) VALUES ('Grass', 2.5, false)");

            // Initialize Court
            jdbc.execute("INSERT INTO court (court_number, surface_type_id, deleted) VALUES (101, 1, false)");
            jdbc.execute("INSERT INTO court (court_number, surface_type_id, deleted) VALUES (102, 1, false)");
            jdbc.execute("INSERT INTO court (court_number, surface_type_id, deleted) VALUES (103, 1, false)");
            jdbc.execute("INSERT INTO court (court_number, surface_type_id, deleted) VALUES (104, 2, false)");
        }

    }
}
