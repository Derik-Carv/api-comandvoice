package com.derikddev.api_comandvoice.infra.gateway;

import com.derikddev.api_comandvoice.dto.request.ReceptionistRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.UUID;

@Slf4j
@Component
public class N8nReceptionist {
    private final RestClient restClient;
    private final String n8nurl;


    public N8nReceptionist(@Value("${app.integration.n8n.webhook-url-receptionist}") String n8nurl) {
        // setting a timeout to prevent the API from freezing.
        SimpleClientHttpRequestFactory requestFactoryRecept = new SimpleClientHttpRequestFactory();
        requestFactoryRecept.setConnectTimeout(5000);
        requestFactoryRecept.setReadTimeout(60000);

        String trackingId = UUID.randomUUID().toString();
        log.atInfo()
                .setMessage("N8N receptionist attending to new request")
                .addKeyValue("tracking_Id", trackingId)
                .log();
        this.restClient = RestClient.builder()
                .requestFactory(requestFactoryRecept)
                .build();
        this.n8nurl = n8nurl;
    }

    public String sendCommandToRecept(ReceptionistRequest request){
        try {
            String trackingId = UUID.randomUUID().toString();
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
            String trackingId = UUID.randomUUID().toString();
            log.atInfo()
                    .setMessage("Fatal error in comuniction HTTP with n8n")
                    .addKeyValue("tracking_Id", trackingId)
                    .log();
            throw new RuntimeException("Fatal error in comuniction HTTP with n8n.", e);
        }
    }
}
