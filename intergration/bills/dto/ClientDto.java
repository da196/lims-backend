package tz.go.tcra.lims.intergration.bills.dto;

import lombok.Data;

@Data
public class ClientDto {

	private ClientType clientType;

	private String name;

	private String email;

	private String phone;

	private String code;

	private String address;

}
