package com.derikddev.api_comandvoice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@NoArgsConstructor
public class CommandVoiceRequest {

    @NotBlank(message = "The voice command text cannot be empty.")
    private String comand;

    @NotBlank(message = "The voice command text cannot be empty.")
    private String system;

    @NotBlank(message = "The voice command text cannot be empty.")
    @Size(min = 3, max = 500, message = "The command must contain a minimum of 3 and a maximum of 500 characters.")
    private String plataform;

    @Override
    public String toString() {
        return "ComandVoiceRequest{" +
                "comand='" + comand + '\'' +
                ", system='" + system + '\'' +
                ", plataform='" + plataform + '\'' +
                '}';
    }
}
