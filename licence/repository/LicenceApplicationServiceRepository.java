/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.licence.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import tz.go.tcra.lims.entity.LicenceApplicationServiceDetail;

/**
 *
 * @author emmanuel.mfikwa
 */
@Repository
public interface LicenceApplicationServiceRepository extends JpaRepository<LicenceApplicationServiceDetail, Long> {

	@Modifying
	@Query("DELETE FROM LicenceApplicationServiceDetail e WHERE e.licenseId=?1")
	void deleteByLicenseId(Long license);

	Set<LicenceApplicationServiceDetail> findByLicenseId(Long licenseId);

	// List<LicenceApplicationServiceDetail> findByLicenseId(Long id);
}
