package tz.go.tcra.lims.licence.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LicenceApplicationEntityMaxDto {

	private Long id;
	private String name;
	private String email;
	private String tin;
}
