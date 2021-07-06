/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.product.service;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import tz.go.tcra.lims.product.dto.EntityProductDto;
import tz.go.tcra.lims.product.dto.EntityProductMaxDto;
import tz.go.tcra.lims.product.entity.EntityProduct;
import tz.go.tcra.lims.utils.Response;

/**
 *
 * @author emmanuel.mfikwa
 */
public interface EntityProductService {
    
    Response<EntityModel<EntityProduct>> saveEntityProduct(EntityProductDto data, Long id);

    Response<EntityProductMaxDto> findEntityProductById(Long id);

    Response<EntityModel<EntityProduct>> activateDeactivateEntityProductById(Long id);

    Response<CollectionModel<EntityModel<EntityProduct>>> getListOfEntityProducts(String keyword, Pageable pageable);

    Response<CollectionModel<EntityModel<EntityProduct>>> getAllEntityProducts();
}
