package com.derikddev.api_comandvoice.rest.wppControllers;

import com.derikddev.api_comandvoice.dto.database.Stage;
import com.derikddev.api_comandvoice.model.repository.StageRepository;
import com.derikddev.api_comandvoice.service.CommandVoiceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/stage")
@RequiredArgsConstructor
public class StageController {
    private final CommandVoiceService commandVoiceService;
    private final StageRepository stageRepository;

    @GetMapping
    public ResponseEntity<List<Stage>> getAllStages(){
        String trackingId = UUID.randomUUID().toString();
        log.atInfo()
                .setMessage("Requesting all stages.")
                .addKeyValue("trackingId", trackingId)
                .log();
        List<Stage> stages = stageRepository.findAll();
        return ResponseEntity.ok(stages);
    }

}
