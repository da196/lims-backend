package tz.go.tcra.lims.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author DonaldSj
 */

@Entity
@Table(name = "lims_licence_individual_applications", schema = "lims")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IndividualLicenceApplicationDetail implements Serializable {

    private static final long serialVersionUID = -6199245727668734490L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "uuid", unique = true)
    private UUID uuid = UUID.randomUUID();

    @OneToOne
    @JoinColumn(name = "license_id",unique=true)
    private Licence license;

    @Column(name = "include_spectrum")
    private Boolean includeSpectrum;

    @Column(name = "include_spectrum_required")
    private Boolean includeSpectrumRequired;

    @Column(name = "spectrum_value")
    private Double spectrumValue;

    @Column(name = "request_description")
    private String requestDescription;

    @Column(name = "investment_value")
    private Double investmentCost;

    @Column(name = "investment_cost_currency")
    private String investmentCostCurrency;

    @Column(name = "commencement_date")
    private Date commencementDate;

    @Column(name = "other_relevant_information")
    private String otherRelevantInfo;
    
    @Column(name = "satellite_uplink_required")
    private Boolean satelliteUplinkRequired;
    
    @Column(name = "beaming_satellite_location")
    private String beamingSatelliteLocation;
    
    @Column(name = "beaming_satellite_latitude")
    private String beamingSatelliteLatitude;
    
    @Column(name = "beaming_satellite_longitude")
    private String beamingSatelliteLongitude;
    
    @Column(name = "transmitter_facility_lesser")
    private String transmitterFacilityLesser;
    
    @Column(name = "facility_owner_category")
    private Long facilityOwnerCategory;
    
    @OneToMany(mappedBy="individualId")
    private List<IndividualLicenceApplicationContentResource> contentResources;
}
