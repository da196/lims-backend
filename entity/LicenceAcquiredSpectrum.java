package tz.go.tcra.lims.entity;

import java.io.Serializable;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author DonaldSj
 */

@Entity
@Table(name = "lims_licence_acquired_spectrum_values", schema = "lims")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class LicenceAcquiredSpectrum implements Serializable {

    private static final long serialVersionUID = -2029799457121426097L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name="licence_id")
    private Long licenceId;
    
    @Column(name = "lower_band")
    private Double lowerBand;
    
    @Column(name = "upper_band")
    private Double upperBand;
    
    @Column(name = "created_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @CreationTimestamp
    private LocalDateTime createdAt=LocalDateTime.now();

    @Column(name = "updated_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "uuid", unique = true)
    private UUID uuid = UUID.randomUUID();
}
