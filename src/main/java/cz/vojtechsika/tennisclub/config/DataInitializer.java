package cz.vojtechsika.tennisclub.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class DataInitializer implements CommandLineRunner {

    private JdbcTemplate jdbc;

    @Autowired
    public DataInitializer(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Value("${app.init.data}")
    private boolean initData;

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
