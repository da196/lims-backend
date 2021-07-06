/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.product.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tz.go.tcra.lims.miscellaneous.enums.EntityApplicationTypeEnum;
import tz.go.tcra.lims.product.entity.EntityProduct;

/**
 *
 * @author emmanuel.mfikwa
 */
public interface EntityProductRepository extends JpaRepository<EntityProduct,Long>{
    
    boolean existsByNameOrCodeAndActive(String name, String code, boolean active);

    boolean existsByIdAndActive(Long id, boolean active);

    EntityProduct findByIdAndActive(Long id, boolean active);
    
    Optional<EntityProduct> findFirstByApplicationTypeAndActive(EntityApplicationTypeEnum applicationType, boolean active);

    List<EntityProduct> findByActive(boolean active);

    Page<EntityProduct> findByActive(boolean b, Pageable pageable);

    @Query("SELECT b FROM EntityProduct b WHERE  " + "CONCAT(b.name,b.code,b.displayName,b.active)" + " LIKE %?1%")
    Page<EntityProduct> findByKeywordActive(String keyword, boolean b, Pageable pageable);
}
