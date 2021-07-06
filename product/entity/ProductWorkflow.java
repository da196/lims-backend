package tz.go.tcra.lims.product.entity;

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
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Any;
import org.hibernate.annotations.AnyMetaDef;
import org.hibernate.annotations.MetaValue;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;
import tz.go.tcra.lims.miscellaneous.enums.ProductTypeEnum;
import tz.go.tcra.lims.workflow.entity.Workflow;

/**
 * @author DonaldSj
 */

@Entity
@Table(name = "lims_product_workflows", schema = "lims")
@NoArgsConstructor
@Getter
@Setter
public class ProductWorkflow implements Serializable{

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "workflow_id")
    private Workflow workflow;

    @JsonIgnore
    @AnyMetaDef(
        name = "ProductMetaDef",
        idType = "long",
        metaType = "string",
        metaValues = {
            @MetaValue(targetEntity = LicenceProduct.class, value = "LICENCE"),
            @MetaValue(targetEntity = EntityProduct.class, value = "ENTITY")
        }
    )
    @Any(
        metaDef = "ProductMetaDef",
        metaColumn = @Column(name = "productable_type")
    )
    @JoinColumn(name = "productable_id")
    private Productable productable;
    
    @Column(name = "productable_id",insertable = false,updatable = false)
    private Long productableId;
    
    @Column(name = "productable_type",insertable = false,updatable = false)
    @Enumerated(EnumType.STRING)
    private ProductTypeEnum productableType;
    
    @Column(name="active")
    @Where(clause="active=true")
    private Boolean active=true;
    
    @JsonIgnore
    @Column(name = "created_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @CreationTimestamp
    private LocalDateTime createdAt=LocalDateTime.now();
    
    @JsonIgnore
    @Column(name = "updated_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @UpdateTimestamp
    private LocalDateTime updateAt=LocalDateTime.now();
    
    @JsonIgnore
    @Column(name = "uuid", unique = true)
    private UUID uuid = UUID.randomUUID();
}
