package tz.go.tcra.lims.miscellaneous.enums;

public enum BillingStatusEnum {

	BILLED, // bill is just generated
	PENDING, // Control number is generated but not paid
	NOTPAID, // Control number expired
	PAID, // paid
	BILLCANCELLED
}
