package tz.go.tcra.lims.geolocation.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor

public class GeoLocationDto {

	private String code;

	private String name;

	private Long parentId;

	private GeoLocationType type;
}
