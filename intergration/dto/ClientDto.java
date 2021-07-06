package tz.go.tcra.lims.intergration.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ClientDto {

	private ClientType clientType;

	private String name;

	private String email;

	private String phone;

	private String code;

	private String address;

}
