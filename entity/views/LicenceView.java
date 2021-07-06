package tz.go.tcra.lims.entity.views;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author DonaldSj
 */

@Entity
@Table(name = "lims_all_licence_view", schema = "lims")
@Setter
@Getter
@NoArgsConstructor
public class LicenceView implements Serializable {

    @Id
    @Column(name = "id", updatable = false, insertable = false)
    private Long id;

    @Column(name = "applicationnumber")
    private String applicationNumber;

    @Column(name = "uuid", unique = true)
    private UUID uuid;

    @Column(name = "applicantentityname")
    private String applicantEntityName;

    @Column(name = "licenseproduct")
    private String licenseProduct;

    @Column(name = "duration")
    private Integer duration;

    @Column(name = "licenceprocessdays")
    private Integer licenceProcessDays;

    @Column(name = "licencenumber")
    private String licenceNumber;

    @Column(name = "applicationdate")
    private LocalDateTime applicationDate;

    @Column(name = "issueddate")
    private LocalDate issuedDate;

    @Column(name = "expiredate")
    private LocalDate expireDate;

    @Column(name = "licencestate")
    private String licenceState;

    @Column(name = "dayslefttoexpire")
    private Long daysLeftToExpire;

    @Column(name = "applicationtype")
    private String applicationType;
}
