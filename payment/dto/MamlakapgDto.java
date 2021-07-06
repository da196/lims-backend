package tz.go.tcra.lims.payment.dto;

import java.io.Serializable;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
@JacksonXmlRootElement(localName = "mamlakapg")
public class MamlakapgDto implements Serializable {

	private static final long serialVersionUID = 21L;

	@JacksonXmlProperty(isAttribute = true)
	private int id;

	@JacksonXmlProperty
	private String BillId;

	@JacksonXmlProperty
	private String TrxSts;

	@JacksonXmlProperty
	private String PayCntrNum;

	@JacksonXmlProperty
	private String TrxStsCode;

}
