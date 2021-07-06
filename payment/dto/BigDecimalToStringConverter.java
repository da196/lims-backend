package tz.go.tcra.lims.payment.dto;

import java.math.BigDecimal;

public interface BigDecimalToStringConverter {
	String asWords(BigDecimal value);
}
