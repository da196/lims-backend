/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.licence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tz.go.tcra.lims.entity.LicenceApplicationEntity;

/**
 *
 * @author emmanuel.mfikwa
 */
@Repository
public interface LicenceApplicationEntityRepository extends JpaRepository<LicenceApplicationEntity, Long> {

	List<LicenceApplicationEntity> findByReferingEntityId(Long id);

}
