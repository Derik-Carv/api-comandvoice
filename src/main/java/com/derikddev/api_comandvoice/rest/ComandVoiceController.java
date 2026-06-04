package com.derikddev.api_comandvoice.rest;

import com.derikddev.api_comandvoice.dto.request.ComandVoiceRequest;
import com.derikddev.api_comandvoice.service.ComandVoiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("api/comands")
@RequiredArgsConstructor
public class ComandVoiceController {

    private final ComandVoiceService comandVoiceService;

    @PostMapping
    public ResponseEntity<String> entryPointVoiceComand(@Valid @RequestBody ComandVoiceRequest request){
        String responseAuto = comandVoiceService.forwardProcess(request);
        String trackingId = UUID.randomUUID().toString();

        log.atInfo()
                .setMessage("Validate dates in Response Entity")
                .addKeyValue("tracking_Id", trackingId)
                .log();

        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(responseAuto);
    }

}
