package com.derikddev.api_comandvoice.model.plataforms;

public interface StrategyPlatform {
    boolean conditionPlatform(String platform);

    void execCustomCommand(Object request);
}