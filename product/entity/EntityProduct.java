package tz.go.tcra.lims.product.entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tz.go.tcra.lims.feestructure.entity.FeeStructure;
import tz.go.tcra.lims.feestructure.entity.Feeable;
import tz.go.tcra.lims.miscellaneous.enums.EntityApplicationTypeEnum;
import tz.go.tcra.lims.miscellaneous.enums.FeeableTypeEnum;
import tz.go.tcra.lims.miscellaneous.enums.ProductTypeEnum;

/**
 * @author DonaldSj
 */

@Entity
@Table(name = "lims_entity_products", schema = "lims")
@NoArgsConstructor
@Getter
@Setter
public class EntityProduct implements Serializable,Feeable,Productable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JsonIgnore
    @Column(name = "uuid", unique = true)
    private UUID uuid = UUID.randomUUID();

    @Column(name = "code",unique=true)
    private String code;

    @Column(name = "name",unique=true)
    private String name;

    @Column(name = "application_type")
    @Enumerated(EnumType.STRING)
    private EntityApplicationTypeEnum applicationType;
    
    @Column(name = "display_name", nullable = true)
    private String displayName;

    @Column(name = "description", nullable = true)
    private String description;

    @JsonIgnore
    @OneToMany
    @JoinColumn(name = "feeable_id", foreignKey = @ForeignKey(name = "none"))
    @Where(clause = "feeable_type = 'ENTITY_PRODUCT'")
    private List<FeeStructure> feeStructures;
    
    @JsonIgnore
    @OneToMany
    @JoinColumn(name = "productable_id", foreignKey = @ForeignKey(name = "none"))
    @Where(clause = "productable_type = 'ENTITY'")
    private List<ProductWorkflow> workflows;

    @Column(name = "active")
    private Boolean active=true;

    @Column(name="created_by")
    private Long createdBy;
    
    @Column(name = "created_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @CreationTimestamp
    private LocalDateTime createdAt=LocalDateTime.now();
    
    @Column(name="updated_by")
    private Long updatedBy;
    
    @Column(name = "updated_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Basic(optional = true)
    @Where(clause = "deleted_at is null")
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @JsonIgnore
    @Override
    public FeeableTypeEnum feeAbleType() {
        
        return FeeableTypeEnum.ENTITY_PRODUCT;
    }

    @Override
    public ProductTypeEnum getProductType() {
        return ProductTypeEnum.ENTITY;
    }
}
