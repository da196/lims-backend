package tz.go.tcra.lims.geolocation.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class GeoLocationMinDto {
    
    private Long id;
    private String code;
    private String name;
}
