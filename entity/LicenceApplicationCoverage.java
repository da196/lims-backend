package tz.go.tcra.lims.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

/**
 * @author DonaldSj
 */

@Entity
@Table(name = "lims_licence_application_coverages", schema = "lims")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class LicenceApplicationCoverage implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "license_id")
    private Long licenseId;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private GeoLocation location;  
    
    @Column(name="active")
    private boolean status=true;
    
    @Column(name="date_created")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @CreationTimestamp
    private LocalDateTime createdAt=LocalDateTime.now();
    
    @JsonIgnore
    @Column(name="unique_id")
    private UUID uniqueID=UUID.randomUUID();
}
