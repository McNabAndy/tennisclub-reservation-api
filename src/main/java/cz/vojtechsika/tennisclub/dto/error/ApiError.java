package cz.vojtechsika.tennisclub.dto.error;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ApiError {

    // Možna toto POJO nevyužiju uvidíme zatím pro sychr

    private int status;
    private String message;
    private long timestamp;

    public ApiError(int status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp = System.currentTimeMillis();
    }
}
