package com.derikddev.api_comandvoice.model.repository;

import com.derikddev.api_comandvoice.dto.database.Stage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StageRepository extends JpaRepository<Stage, String> { // Alterado para String aqui

    Stage findByStageKey(String stageKey);

    Stage findByDescription(String description);

}