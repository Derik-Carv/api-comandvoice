package com.derikddev.api_comandvoice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@NoArgsConstructor
public class ComandVoiceRequest {

    @NotBlank(message = "O texto do comando de voz não pode estar vazio.")
    private String comand;

    @NotBlank(message = "O texto do comando de voz não pode estar vazio.")
    private String system;

    @NotBlank(message = "O texto do comando de voz não pode estar vazio.")
    @Size(min = 3, max = 500, message = "O comando deve conter o minimo de 3 e o maximo e 500 caracteres.")
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
