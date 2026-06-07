package com.derikddev.api_comandvoice.model.plataforms;

import com.derikddev.api_comandvoice.dto.request.CommandVoiceRequest;
import com.derikddev.api_comandvoice.dto.request.ReceptionistRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class WhatsappPlatform implements StrategyPlatform {

    @Override
    public boolean conditionPlatform(String platform) {
        String trackingId = UUID.randomUUID().toString();
        log.atInfo()
                .setMessage("Expected platform: WHATSAPP")
                .addKeyValue("tracking_Id", trackingId)
                .log();
        return platform.contains("WHATSAPP");
    }

    @Override
    public void execCustomCommand(Object request) {
        String trackingId = UUID.randomUUID().toString();
        try {
            if (request instanceof CommandVoiceRequest validRequest) {
                log.atInfo()
                        .setMessage("Exec voice commands WHATSAPP - Context: CommandVoiceRequest")
                        .addKeyValue("tracking_Id", trackingId)
                        .log();

                validRequest.setComand(validRequest.getComand() + " (Detected WHATSAPP)");
            }
            else if (request instanceof ReceptionistRequest validRequest) {
                log.atInfo()
                        .setMessage("Exec voice commands WHATSAPP - Context: ReceptionistRequest")
                        .addKeyValue("tracking_Id", trackingId)
                        .log();

                validRequest.setBody(validRequest.getBody() + " (Detected WHATSAPP)");
            }
            else {
                log.atWarn()
                        .setMessage("Object type received is not supported by WhatsappPlatform")
                        .addKeyValue("tracking_Id", trackingId)
                        .log();
            }
        } catch (Exception e) {
            log.atError()
                    .setMessage("Error during platform commands execution for WHATSAPP")
                    .addKeyValue("tracking_Id", trackingId)
                    .setCause(e)
                    .log();
            throw new RuntimeException("Error verification compatibility with WHATSAPP, not supported", e);
        }
    }
}