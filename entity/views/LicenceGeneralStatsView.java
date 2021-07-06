package tz.go.tcra.lims.entity.views;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author DonaldSj
 */

@Entity
@Table(name = "licence_report_general_stats_view", schema = "lims")
@Setter
@Getter
@NoArgsConstructor
public class LicenceGeneralStatsView implements Serializable {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "totallicences")
    private long totalLicences;

    @Column(name = "cancelledlicense")
    private long cancelledLicense;

    @Column(name = "suspendedlicense")
    private long suspendedLicense;

    @Column(name = "expiredlicense")
    private long expiredLicense;

    @Column(name = "activelicense")
    private long activeLicense;

    @Column(name = "newlicences")
    private long newLicences;
}
