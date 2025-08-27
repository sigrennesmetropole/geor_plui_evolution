package org.georchestra.pluievolution.core.entity.configuration;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

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
