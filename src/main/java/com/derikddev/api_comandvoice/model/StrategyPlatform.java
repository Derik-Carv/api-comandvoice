package com.derikddev.api_comandvoice.model;

import com.derikddev.api_comandvoice.dto.request.ComandVoiceRequest;

public interface StrategyPlatform {
    boolean conditionPlataform(String plaform);

    void execCustomComand (ComandVoiceRequest request);
}
