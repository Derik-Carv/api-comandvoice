package com.derikddev.api_comandvoice.dto.database;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "stage_types")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Stage {

    @Id
    @Column(name = "stage_key", length = 100, nullable = false)
    private String stageKey;

    @Column(name = "description", length = 255, nullable = false)
    private String description;
}