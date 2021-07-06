package tz.go.tcra.lims.entity.views;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author DonaldSj
 */

@Entity
@Table(name = "lims_payment_report_view", schema = "lims")
@Setter
@Getter
@NoArgsConstructor
public class PaymentReportView implements Serializable {

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "billamount")
    private Double billAmount;

    @Column(name = "paidamount")
    private Double paidAmount;

    @Column(name = "paymode")
    private String payMode;

    @Column(name = "billdate")
    private LocalDateTime billDate;

    @Column(name = "paydate")
    private LocalDateTime payDate;

}
