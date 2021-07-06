package tz.go.tcra.lims.feestructure.entity;

import tz.go.tcra.lims.product.entity.LicenceProduct;
import tz.go.tcra.lims.product.entity.EntityProduct;
import tz.go.tcra.lims.miscellaneous.enums.FeePeriodEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.*;
import tz.go.tcra.lims.uaa.entity.LimsUser;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;
import tz.go.tcra.lims.entity.Licence;
import tz.go.tcra.lims.miscellaneous.entity.ListOfValue;
import tz.go.tcra.lims.miscellaneous.enums.ApplicableStateEnum;
import tz.go.tcra.lims.miscellaneous.enums.FeeableTypeEnum;

/**
 * @author DonaldSj
 */

@Entity
@Table(name = "lims_fee_structures", schema = "lims")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FeeStructure implements Serializable {

    private static final long serialVersionUID = 5777476284313619305L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JsonIgnore
    @Column(name = "uuid", unique = true)
    private UUID uuid = UUID.randomUUID();

    @Column(name = "name",unique=true)
    private String name;

    @Column(name = "code",unique=true)
    private String code;

    @Column(name = "account_code",unique=true)
    private String accountCode;
    
    @JsonIgnore
    @Any(
            metaDef = "FeeStructureMetaDef",
            metaColumn = @Column(name = "feeable_type")
    )
    @AnyMetaDef(
            name = "FeeStructureMetaDef",
            idType = "long",
            metaType = "string",
            metaValues = {
                    @MetaValue(targetEntity = Licence.class, value = "LICENSE"),
                    @MetaValue(targetEntity = LicenceProduct.class, value = "LICENSE_PRODUCT"),
                    @MetaValue(targetEntity = EntityProduct.class, value = "ENTITY_PRODUCT"),
                    @MetaValue(targetEntity = LimsUser.class, value = "USER"),
            }
    )
    @JoinColumn(name = "feeable_id")
    private Feeable feeable;

    @Column(name = "feeable_id",insertable = false,updatable = false)
    private Long feeableId;

    @Column(name = "feeable_type",insertable = false,updatable = false)
    @Enumerated(EnumType.STRING)
    private FeeableTypeEnum feeableType;
        
    @Column(name = "applicable_state")
    @Enumerated(EnumType.STRING)
    private ApplicableStateEnum applicableState;

    @ManyToOne
    @JoinColumn(name = "fee_currency")
    private ListOfValue feeCurrency;

    @Column(name = "fee_amount")
    private Double feeAmount;

    @Column(name = "fee_percent")
    private Double feePercent;

    @Column(name = "frequency")
    private Integer frequency;

    @Column(name = "period")
    @Enumerated(EnumType.STRING)
    private FeePeriodEnum period;

    @Column(name = "active")
    private Boolean active=true;

    @OneToOne
    @JoinColumn(name = "fee_type")
    private ListOfValue feeType;

    @Column(name="created_by")
    private Long createdBy;
    
    @JsonIgnore
    @Column(name = "created_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @CreationTimestamp
    private LocalDateTime createdAt=LocalDateTime.now();

    @Column(name="updated_by")
    private Long updatedBy;
    
    @JsonIgnore
    @Column(name = "updated_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @JsonIgnore
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}
