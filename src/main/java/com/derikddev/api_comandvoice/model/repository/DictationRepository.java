package com.derikddev.api_comandvoice.model.repository;

import com.derikddev.api_comandvoice.dto.database.Dictation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DictationRepository extends JpaRepository<Dictation, String> {
    Dictation findByBody(String body);

    Dictation findByPlatformReceived(String platformReceived);

    Dictation findBySystemReceived(String systemReceived);

}
