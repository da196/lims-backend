/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.miscellaneous.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import tz.go.tcra.lims.miscellaneous.enums.MinistryEnum;

/**
 *
 * @author emmanuel.mfikwa
 */
@Entity
@Table(name = "lims_ministry_consultation_letter_config", schema = "lims")
@Data
@NoArgsConstructor
public class ConsultationLetterConfig implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "address")
    private String address;
    
    @Column(name = "activity")
    private String activity;
    
    @Column(name = "ministry_name")
    private String ministryName;
    
    @Column(name = "flag")
    @Enumerated(EnumType.STRING)
    private MinistryEnum flag;
}
