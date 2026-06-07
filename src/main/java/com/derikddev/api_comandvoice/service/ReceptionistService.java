package com.derikddev.api_comandvoice.service;

import com.derikddev.api_comandvoice.dto.request.ReceptionistRequest;
import com.derikddev.api_comandvoice.infra.gateway.N8nReceptionist;
import com.derikddev.api_comandvoice.model.plataforms.StrategyPlatform;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReceptionistService {

    private final List<StrategyPlatform> platformStrategy;
    private final N8nReceptionist n8nReceptionist;

    public String receptionistProcess(ReceptionistRequest request){
        String platformReceived = request.toString();

        String trackingId = UUID.randomUUID().toString();

        log.atInfo()
                .setMessage("Starting command voice process")
                .addKeyValue("tracking_Id", trackingId)
                .addKeyValue("platform", platformReceived)
                .log();

        try {
            platformStrategy.stream()
                    // filter list find class responde with true
                    .filter(strategy -> strategy.conditionPlatform(platformReceived))
                    // received firist response true
                    .findFirst()
                    // if it`ok - execute functions plataform
                    .ifPresentOrElse(
                            // if response
                            strategy -> strategy.execCustomCommand(request),
                            // if not`s exist
                            () -> log.atInfo()
                                    .setMessage("Stratyless")
                                    .addKeyValue("tracking_Id", trackingId)
                                    .log()
                    );

            log.atInfo()
                    .setMessage("Starting communication with n8n")
                    .addKeyValue("tracking_Id", trackingId)
                    .log();
            // exec n8n communication.
            return n8nReceptionist.sendCommandToRecept(request);
        } catch (Exception e) {
            log.atInfo()
                    .setMessage("Error with command voice with n8n")
                    .addKeyValue("tracking_Id", trackingId)
                    .log();
            throw new RuntimeException(e);
        }

    }
}
