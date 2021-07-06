/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.licencee.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tz.go.tcra.lims.licencee.entity.EntityCategory;

/**
 *
 * @author emmanuel.mfikwa
 */
@Repository
public interface EntityCategoryRepository extends JpaRepository<EntityCategory, Long> {

	public Optional<EntityCategory> findEntityCategoryById(Long id);

	public Optional<EntityCategory> findByName(String name);

	public List<EntityCategory> findByActive(boolean status);

	public boolean existsByIdAndActive(Long applicantId, boolean b);
}
