package tz.go.tcra.lims.communications.dto;

import lombok.Data;

@Data
public class SmsDto {
	String from;
	String to;
	String text;

}
