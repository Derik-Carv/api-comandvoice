package com.derikddev.api_comandvoice.rest.commands;

import com.derikddev.api_comandvoice.dto.request.CommandVoiceRequest;
import com.derikddev.api_comandvoice.dto.request.ReceptionistRequest;
import com.derikddev.api_comandvoice.service.CommandVoiceService;
import com.derikddev.api_comandvoice.service.ReceptionistService;
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
public class CommandVoiceController {

    private final ReceptionistService receptionistService;

    @PostMapping("/receptionist")
    public ResponseEntity<String> receptionVoiceCommando(@Valid @RequestBody ReceptionistRequest request){
        String responseAuto = receptionistService.receptionistProcess(request);

        String trackingId = UUID.randomUUID().toString();

        log.atInfo()
                .setMessage("Validate dates in receptionist Entity")
                .addKeyValue("tracking_Id", trackingId)
                .log();
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(responseAuto);
    };

    private final CommandVoiceService commandVoiceService;

    @PostMapping
    public ResponseEntity<String> entryPointVoiceCommand(@Valid @RequestBody CommandVoiceRequest request){

        String responseAuto = commandVoiceService.forwardProcess(request);
        String trackingId = UUID.randomUUID().toString();

        log.atInfo()
                .setMessage("Validate dates in Response Entity")
                .addKeyValue("tracking_Id", trackingId)
                .log();

        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(responseAuto);
    }

}
