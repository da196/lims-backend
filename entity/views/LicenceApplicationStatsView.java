package tz.go.tcra.lims.entity.views;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * @author DonaldSj
 */

@Entity
@Table(name = "licence_application_stats_view", schema = "lims")
@Setter
@Getter
@NoArgsConstructor
public class LicenceApplicationStatsView implements Serializable {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "totalapplications")
    private long totalApplications;

    @Column(name = "newapplications")
    private long newApplications;

    @Column(name = "inprogressapplications")
    private long inProgressApplications;

    @Column(name = "underministryapplications")
    private long underMinistryApplications;

    @Column(name = "rejectedapplications")
    private long rejectedApplications;
}
