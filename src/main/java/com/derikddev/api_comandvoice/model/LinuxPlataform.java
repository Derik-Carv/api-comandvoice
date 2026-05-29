package com.derikddev.api_comandvoice.model;

import com.derikddev.api_comandvoice.dto.request.ComandVoiceRequest;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class LinuxPlataform implements StrategyPlatform{

    String trackingId = UUID.randomUUID().toString();

    @Override
    public boolean conditionPlataform(String plaform) {
        log.atInfo()
                .setMessage("expected plataform: LINUX")
                .addKeyValue("tracking_Id", trackingId)
                .log();
        return plaform.contains("Linux x86_64");
    };

    @Override
    public void execCustomComand(@NonNull ComandVoiceRequest request){
        log.atInfo()
                .setMessage("Executando comandos de voz compatíveis com: Linux")
                .addKeyValue("tracking_Id", trackingId)
                .log();
        request.setComand(request.getComand() + "(Detected Linux OS)");
    };

}
