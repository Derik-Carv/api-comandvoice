package com.derikddev.api_comandvoice.dto.database;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "stage_types")
@Getter                 // Usando o Lombok para evitar poluir o código com Getters/Setters manuais
@Setter
@NoArgsConstructor      // OBRIGATÓRIO para o Hibernate conseguir instanciar a classe
@AllArgsConstructor
@Builder
@ToString
public class Stage {

    @Id
    // REMOVIDO o @GeneratedValue porque no banco o VARCHAR é inserido manualmente
    @Column(name = "stage_key", length = 100, nullable = false)
    private String stageKey; // Alterado de long para String para casar com VARCHAR

    @Column(name = "description", length = 255, nullable = false)
    private String description; // Deixando em camelCase no Java e mapeando com as convenções padrão
}