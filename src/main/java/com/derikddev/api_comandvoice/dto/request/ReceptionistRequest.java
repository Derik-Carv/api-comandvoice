package com.derikddev.api_comandvoice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ReceptionistRequest {
    @NotBlank(message = "The voice command text cannot be empty.")
    private String body;

    @NotBlank(message = "The voice command text cannot be empty.")
    @Size(min = 3, max = 500, message = "The command must contain a minimum of 3 and a maximum of 500 characters.")
    private String platform;

    @NotBlank(message = "The voice command text cannot be empty.")
    private String system;

    @Override
    public String toString() {
        return "ReceptionistRequest{" +
                "body='" + body + '\'' +
                ", platform='" + platform + '\'' +
                ", system='" + system + '\'' +
                '}';
    }
}
