/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.portal.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tz.go.tcra.lims.miscellaneous.dto.ListOfValueMinDto;

/**
 *
 * @author emmanuel.mfikwa
 */
@NoArgsConstructor
@Setter
@Getter
public class IndividualLicenceDto {
    
    private Boolean includeSpectrum;
    private Boolean includeSpectrumRequired;
    private String requestDescription;
    private Double investmentCost;
    private String investmentCostCurrency;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date commencementDate;
    private String otherRelevantInfo;
    private Boolean satelliteUplinkRequired;
    private String beamingSatelliteLocation;
    private String beamingSatelliteLatitude;
    private String beamingSatelliteLongitude;
    private String transmitterFacilityLesser;
    private ListOfValueMinDto facilityOwnerCategory;
    private List<ListOfValueMinDto> resources;
}
