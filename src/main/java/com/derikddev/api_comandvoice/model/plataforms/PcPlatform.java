package com.derikddev.api_comandvoice.model.plataforms;

import com.derikddev.api_comandvoice.dto.request.CommandVoiceRequest;
import com.derikddev.api_comandvoice.dto.request.ReceptionistRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class PcPlatform implements StrategyPlatform {

    @Override
    public boolean conditionPlatform(String platform) {
        return platform.contains("PC");
    }

    @Override
    public void execCustomCommand(Object request) {
        String trackingId = UUID.randomUUID().toString();
        try {
            if (request instanceof CommandVoiceRequest validRequest) {
                log.atInfo()
                        .setMessage("Exec voice commands PC - Context: CommandVoiceRequest")
                        .addKeyValue("tracking_Id", trackingId)
                        .log();

                validRequest.setComand(validRequest.getComand() + " (Detected PC)");
            }
            else if (request instanceof ReceptionistRequest validRequest) {
                log.atInfo()
                        .setMessage("Exec voice commands PC - Context: ReceptionistRequest")
                        .addKeyValue("tracking_Id", trackingId)
                        .log();
                validRequest.setBody(validRequest.getBody() + " (Detected PC)");
            }
            else {
                log.atWarn()
                        .setMessage("Object type received is not supported by PcPlatform")
                        .addKeyValue("tracking_Id", trackingId)
                        .log();
            }
        } catch (Exception e) {
            log.atError()
                    .setMessage("Error during platform commands execution for PC")
                    .addKeyValue("tracking_Id", trackingId)
                    .setCause(e)
                    .log();
            throw new RuntimeException("Error verification compatibility with PC, not supported", e);
        }
    }
}