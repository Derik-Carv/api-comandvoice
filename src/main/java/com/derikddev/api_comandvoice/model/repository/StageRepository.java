package com.derikddev.api_comandvoice.model.repository;

import com.derikddev.api_comandvoice.dto.database.Stage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StageRepository extends JpaRepository<Stage, Long> {

    Stage findByStageKey (Long id);

    Stage finddByDescription (String description);

}
