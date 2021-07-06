package tz.go.tcra.lims.payment.entity;

import java.io.Serializable;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tz.go.tcra.lims.miscellaneous.entity.ListOfValue;

/**
 * @author DonaldSj
 */

@Entity
@Table(name = "lims_exchange_rate", schema = "lims")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRate implements Serializable {

	private static final long serialVersionUID = -2029799457121426097L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	@Column(name = "amount")
	private Double amount;

	@Column(name = "uuid", unique = true)
	private UUID uuid = UUID.randomUUID();

	@OneToOne
	@JoinColumn(name = "currecy_from_id", nullable = false)
	private ListOfValue currecyFrom;

	@OneToOne
	@JoinColumn(name = "currecy_to_id", nullable = false)
	private ListOfValue currecyTo;

	@Column(name = "start_date")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date startDate;

	@Column(name = "expire_date")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date expireDate;

	@Column(name = "created_at")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@CreationTimestamp
	private LocalDateTime createdAt;

	@Column(name = "deleted_at")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@CreationTimestamp
	private LocalDateTime deleteddAt;

	@Column(name = "updated_at")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@UpdateTimestamp
	private LocalDateTime updatedAt;

	@Column(name = "active")
	private Boolean active = true;

}
