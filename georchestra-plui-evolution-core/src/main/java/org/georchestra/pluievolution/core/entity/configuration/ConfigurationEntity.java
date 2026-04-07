package org.georchestra.pluievolution.core.entity.configuration;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "configuration")
public class ConfigurationEntity {

    @Id
    @Column(name = "code", nullable = false, length = 30)
    private String code;

    @Column(name = "valeur", length = 100)
    private String valeur;
}
