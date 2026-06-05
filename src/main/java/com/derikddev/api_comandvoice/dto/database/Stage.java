package com.derikddev.api_comandvoice.dto.database;

import jakarta.persistence.*;

@Entity
@Table(name = "stage_types")
public class Stage {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private long StageKey;

    private String Description;

    public Stage(long stageKey, String description) {
        StageKey = stageKey;
        Description = description;
    }

    @Override
    public String toString() {
        return "Stage{" +
                "StageKey=" + StageKey +
                ", Description='" + Description + '\'' +
                '}';
    }
}
