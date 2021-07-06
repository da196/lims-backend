/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.utils.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import tz.go.tcra.lims.entity.Licence;
import tz.go.tcra.lims.miscellaneous.enums.NumberQueTypeEnum;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author emmanuel.mfikwa
 */
@Entity
@Table(name = "lims_number_generation_que", schema = "lims")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NumberQue implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "licence_id")
    private Licence licence;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private NumberQueTypeEnum type;

    @Column(name = "created_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @CreationTimestamp
    private LocalDateTime createdAt = LocalDateTime.now();
}
