package com.derikddev.api_comandvoice.infra.gateway;

import com.derikddev.api_comandvoice.dto.request.ComandVoiceRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.UUID;

@Slf4j
@Component
public class N8nGateway {

    private final RestClient restClient;
    private final String n8nurl;
    String trackingId = UUID.randomUUID().toString();

    public N8nGateway(@Value("${app.integration.n8n.webhook-url}") String n8nurl) {
        log.atInfo()
                .setMessage("Starting connection with n8n")
                .addKeyValue("tracking_Id", trackingId)
                .log();
        this.restClient = RestClient.create();
        this.n8nurl = n8nurl;
    }

    public String sendComand(ComandVoiceRequest request){
        try {
            log.atInfo()
                    .setMessage("Forward payload for webhook in n8n")
                    .addKeyValue("tracking_Id", trackingId)
                    .log();

            String n8nResponse = restClient.post()
                    .uri(n8nurl)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(request)
                    .retrieve()
                    .body(String.class);

            log.atInfo()
                    .setMessage("Payload integrate with sucess!")
                    .addKeyValue("tracking_Id", trackingId)
                    .log();
            return n8nResponse;
        } catch (Exception e) {
            log.atInfo()
                    .setMessage("Fatal error in comuniction HTTP with n8n")
                    .addKeyValue("tracking_Id", trackingId)
                    .log();
            throw new RuntimeException("Fatal error in comuniction HTTP with n8n.", e);
        }
    }
}
