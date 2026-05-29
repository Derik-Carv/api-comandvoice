package com.derikddev.api_comandvoice.service;

import com.derikddev.api_comandvoice.dto.request.ComandVoiceRequest;
import com.derikddev.api_comandvoice.infra.gateway.N8nGateway;
import com.derikddev.api_comandvoice.model.StrategyPlatform;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ComandVoiceService {

    private final List<StrategyPlatform> plaformStrategy;
    private final N8nGateway n8nGateway;

    public String forwardProcess(ComandVoiceRequest request){
        String plataformReceived = request.getPlataform();

        String trackingId = UUID.randomUUID().toString();

        log.atInfo()
                .setMessage("Starting comand voice process")
                .addKeyValue("tracking_Id", trackingId)
                .addKeyValue("plataform", plataformReceived)
                .log();

        try {
            plaformStrategy.stream()
                    // filter list find class responde with true
                    .filter(strategy -> strategy.conditionPlataform(plataformReceived))
                    // received firist response true
                    .findFirst()
                    // if it`ok - execute functions plataform
                    .ifPresentOrElse(
                            // if response
                            strategy -> strategy.execCustomComand(request),
                            // if not`s exist
                            () -> log.atInfo()
                                    .setMessage("Stratyless")
                                    .addKeyValue("tracking_Id", trackingId)
                                    .log()
                    );

            log.atInfo()
                    .setMessage("Starting comunication with n8n")
                    .addKeyValue("tracking_Id", trackingId)
                    .log();
            // exec n8n comunication.
            return n8nGateway.sendComand(request);
        } catch (Exception e) {
            log.atInfo()
                    .setMessage("Error with comand voice with n8n")
                    .addKeyValue("tracking_Id", trackingId)
                    .log();
            throw new RuntimeException(e);
        }

    }
}
