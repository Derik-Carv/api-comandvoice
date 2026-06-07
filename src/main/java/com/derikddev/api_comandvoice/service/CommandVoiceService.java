package com.derikddev.api_comandvoice.service;

import com.derikddev.api_comandvoice.dto.request.CommandVoiceRequest;
import com.derikddev.api_comandvoice.infra.gateway.N8nGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommandVoiceService {

    private final N8nGateway n8nGateway;

    public String forwardProcess(CommandVoiceRequest request){
        String platformReceived = request.getPlataform();

        String trackingId = UUID.randomUUID().toString();

        log.atInfo()
                .setMessage("Starting command voice process")
                .addKeyValue("tracking_Id", trackingId)
                .addKeyValue("platform", platformReceived)
                .log();

        try {
            log.atInfo()
                    .setMessage("Starting communication with n8n")
                    .addKeyValue("tracking_Id", trackingId)
                    .log();
            // exec n8n communication.
            return n8nGateway.sendCommand(request);
        } catch (Exception e) {
            log.atInfo()
                    .setMessage("Error with command voice with n8n")
                    .addKeyValue("tracking_Id", trackingId)
                    .log();
            throw new RuntimeException(e);
        }

    }
}
