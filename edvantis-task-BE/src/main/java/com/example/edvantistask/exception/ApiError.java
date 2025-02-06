package com.example.edvantistask.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.http.HttpStatusCode;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ApiError {

    private HttpStatusCode status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    private String message;

    private List<String> errors;

    public ApiError(HttpStatusCode status, String message, List<String> errors) {
        this.status = status;
        this.timestamp = LocalDateTime.now();
        this.message = message;
        this.errors = errors;
    }
}
