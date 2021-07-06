package tz.go.tcra.lims.payment.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "lims_mamlakapg", schema = "lims")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Mamlakapg implements Serializable {

	private static final long serialVersionUID = -2029799457121426097L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "bill_id")
	private String BillId;

	@Column(name = "transaction_status")
	private String TrxSts;

	@Column(name = "uuid", unique = true)
	private UUID uuid = UUID.randomUUID();

	@Column(name = "control_number")
	private String PayCntrNum;

	@Column(name = "transaction_code")
	private String TrxStsCode;

	@Column(name = "created_at")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@CreationTimestamp
	private LocalDateTime createdAt;

	@Column(name = "active")
	private Boolean active = true;

}
