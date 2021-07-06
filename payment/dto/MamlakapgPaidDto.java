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

public class MamlakapgPaidDto implements Serializable {

	private static final long serialVersionUID = 21L;

	@JacksonXmlProperty(isAttribute = true)
	private int id;

	@JacksonXmlProperty
	private String BillId;

	@JacksonXmlProperty
	private String TrxID;

	@JacksonXmlProperty
	private String PayCntrNum;

	@JacksonXmlProperty
	private String PayRefId;

	@JacksonXmlProperty
	private String BillAmt;

	@JacksonXmlProperty
	private String PaidAmt;

	@JacksonXmlProperty
	private String BillPayOpt;

	@JacksonXmlProperty
	private String CCy;

	@JacksonXmlProperty
	private String TrxDtTm;

	@JacksonXmlProperty
	private String UsdPayChnl;

	@JacksonXmlProperty
	private String PyrCellNum;

	@JacksonXmlProperty
	private String PyrEmail;

	@JacksonXmlProperty
	private String PyrName;

	@JacksonXmlProperty
	private String PspReceiptNumber;

	@JacksonXmlProperty
	private String PspName;

	@JacksonXmlProperty
	private String CtrAccNum;

}
