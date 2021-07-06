/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.portal.application.dto;

import java.util.List;
import lombok.Data;

/**
 *
 * @author emmanuel.mfikwa
 */
@Data
public class ContentApplicationDto {
    
    private Boolean satelliteUplinkRequired;
    private String beamingSatelliteLocation;
    private String beamingSatelliteLatitude;
    private String beamingSatelliteLongitude;
    private String transmitterFacilityLesser;
    private Long facilityOwnerCategory;
    private List<Long> resources;
}
