package com.derikddev.api_comandvoice.dto.database;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "dictation_command")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Dictation {

    @Id
    @Column(name = "id", length = 100, nullable = false)
    private String id;

    @Column(name = "body", nullable = false)
    private String body;

    @Column(name = "platformReceived", nullable = false)
    private String platformReceived;

    @Column(name = "systemReceived", nullable = false)
    private String systemReceived;

    @Column(name = "newPayload", nullable = true)
    private String newPayload;
}
