package tz.go.tcra.lims.intergration.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@Setter
@Getter
public class SysConfig {

	@Value("${erms.auth.token-url}")
	private String ermsAuthTokenUrl;

	@Value("${erms.invoice.post-url}")
	private String ermsInvoicePostUrl;

	@Value("${erms.receipt.post-url}")
	private String ermsReceiptPostUrl;

	@Value("${erms.invoice.cancel.post-url}")
	private String ermsInvoiceCancelPostUrl;

	@Value("${erms.invoice.expired.post-url}")
	private String ermsInvoiceExpiredPostUrl;

	@Value("${erms.signature.algorithm}")
	private String signatureAlgorithm;

	@Value("${erms.key-store}")
	private String ermsKeyStore;

	@Value("${erms.key-size}")
	private String ermsKeySize;

	@Value("${erms.key-validity}")
	private String ermsKeyValidity;

	@Value("${erms.client.id}")
	private String ermsClientId;

	@Value("${erms.client.secret}")
	private String ermsClientSecret;

	@Value("${erms.auth.grant-type}")
	private String ermsAuthGrantType;

	@Value("${erms.auth.scope}")
	private String ermsAuthScope;

	@Value("${lims.private.key.file}")
	private String limsPrivateKeyFile;

	@Value("${lims.private.key}")
	private String limsPrivateKey;

	@Value("${lims.public.key.file}")
	private String limsPublicKeyFile;
}
