package tz.go.tcra.lims.licence.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import tz.go.tcra.lims.entity.Licence;
import tz.go.tcra.lims.entity.LicenceApplicationEntity;
import tz.go.tcra.lims.licence.dto.LicenseMinDto;
import tz.go.tcra.lims.miscellaneous.enums.LicenceStateEnum;

/**
 * @author DonaldSj
 */

@Repository
public interface LicenceRepository extends JpaRepository<Licence, Long> {

	boolean existsByLicenseStateInAndActive(List<LicenceStateEnum> state, boolean active);

	boolean existsByLicenseStateNotInAndActive(List<LicenceStateEnum> state, boolean active);

	boolean existsByIdAndActive(Long id, boolean active);

	Licence findByIdAndActive(Long id, boolean active);

	List<Licence> findByLicenseStateInAndActive(List<LicenceStateEnum> state, boolean active);

	@Query("SELECT e FROM Licence e WHERE e.applicantEntity.referingEntityId=?1 AND e.licenseState IN ?2 ORDER BY e.createdAt DESC")
	List<Licence> findByLicenceeEntityIdAndLicenseState(Long licenceeEntityId, List<LicenceStateEnum> state);

	List<Licence> findByLicenseStateNotInAndActive(List<LicenceStateEnum> state, boolean active);

	@Query("SELECT e FROM Licence e WHERE e.applicantEntity.referingEntityId=?1 ORDER BY e.createdAt DESC")
	Set<Licence> findByApplicantEntity(Long applicantEntity);

	@Query("SELECT e FROM Licence e WHERE e.applicantEntity.referingEntityId=?1 AND e.decision=?2 ORDER BY e.createdAt DESC")
	Set<Licence> findByApplicantEntityAndDecision(Long applicantEntity, String decision);

	List<Licence> findByActive(boolean active);

	@Query("SELECT new tz.go.tcra.lims.licence.dto.LicenseMinDto(" + "e.id," + "e.applicantEntity.name,"
			+ "e.applicationNumber," + "e.licenceNumber," + "e.licenseState," + "e.applicationState,"
			+ "e.status.displayName," + "e.licenseProduct.id," + "e.licenseProduct.displayName,"
			+ "e.licenseProduct.licenseCategory.id," + "e.licenseProduct.licenseCategory.displayName," + "e.issuedDate,"
			+ "e.expireDate," + "e.submittedAt) FROM Licence e")
	Page<LicenseMinDto> findAllLicences(Pageable pageable);

	@Query("SELECT new tz.go.tcra.lims.licence.dto.LicenseMinDto(" + "e.id," + "e.applicantEntity.name,"
			+ "e.applicationNumber," + "e.licenceNumber," + "e.licenseState," + "e.applicationState,"
			+ "e.status.displayName," + "e.licenseProduct.id," + "e.licenseProduct.displayName,"
			+ "e.licenseProduct.licenseCategory.id," + "e.licenseProduct.licenseCategory.displayName," + "e.issuedDate,"
			+ "e.expireDate," + "e.submittedAt) FROM Licence e")
	Page<LicenseMinDto> findAllLicencesSearchable(Pageable pageable);

	@Query("SELECT new tz.go.tcra.lims.licence.dto.LicenseMinDto(" + "e.id," + "e.applicantEntity.name,"
			+ "e.applicationNumber," + "e.licenceNumber," + "e.licenseState," + "e.applicationState,"
			+ "e.status.displayName," + "e.licenseProduct.id," + "e.licenseProduct.displayName,"
			+ "e.licenseProduct.licenseCategory.id," + "e.licenseProduct.licenseCategory.displayName," + "e.issuedDate,"
			+ "e.expireDate," + "e.submittedAt) FROM Licence e WHERE e.isDraft=false")
	Page<LicenseMinDto> findAllNonDraftLicences(Pageable pageable);

	@Query("SELECT new tz.go.tcra.lims.licence.dto.LicenseMinDto(" + "e.id," + "e.applicantEntity.name,"
			+ "e.applicationNumber," + "e.licenceNumber," + "e.licenseState," + "e.applicationState,"
			+ "e.status.displayName," + "e.licenseProduct.id," + "e.licenseProduct.displayName,"
			+ "e.licenseProduct.licenseCategory.id," + "e.licenseProduct.licenseCategory.displayName," + "e.issuedDate,"
			+ "e.expireDate," + "e.submittedAt) FROM Licence e WHERE e.isDraft=false AND e.licenseProduct.id=?1")
	Page<LicenseMinDto> findAllNonDraftLicencesByProduct(Long productId, Pageable pageable);

	@Query("SELECT new tz.go.tcra.lims.licence.dto.LicenseMinDto(" + "e.id," + "e.applicantEntity.name,"
			+ "e.applicationNumber," + "e.licenceNumber," + "e.licenseState," + "e.applicationState,"
			+ "e.status.displayName," + "e.licenseProduct.id," + "e.licenseProduct.displayName,"
			+ "e.licenseProduct.licenseCategory.id," + "e.licenseProduct.licenseCategory.displayName," + "e.issuedDate,"
			+ "e.expireDate," + "e.submittedAt) FROM Licence e WHERE e.isDraft=false AND e.rootLicenceCategoryId=?1")
	Page<LicenseMinDto> findAllNonDraftLicencesByRoot(Long root, Pageable pageable);

	@Query("SELECT new tz.go.tcra.lims.licence.dto.LicenseMinDto(" + "e.id," + "e.applicantEntity.name,"
			+ "e.applicationNumber," + "e.licenceNumber," + "e.licenseState," + "e.applicationState,"
			+ "e.status.displayName," + "e.licenseProduct.id," + "e.licenseProduct.displayName,"
			+ "e.licenseProduct.licenseCategory.id," + "e.licenseProduct.licenseCategory.displayName," + "e.issuedDate,"
			+ "e.expireDate,"
			+ "e.submittedAt) FROM Licence e WHERE e.isDraft=false AND e.rootLicenceCategoryId=?1 AND e.licenseProduct.id=?2")
	Page<LicenseMinDto> findAllNonDraftLicencesByRootAndProduct(Long root, Long product, Pageable pageable);

	@Query("FROM Licence l WHERE  " + "CONCAT(" + "l.applicationNumber," + "l.licenceNumber" + ")" + " LIKE %?1%")
	Page<Licence> findByKeyword(String keyword, Pageable pageable);

	List<Licence> findByApplicantEntityInAndLicenseState(List<LicenceApplicationEntity> applicantentities,
			LicenceStateEnum active);

	List<Licence> findByApplicantEntityInAndLicenseStateAndDecisionNotInAndIsDraft(
			List<LicenceApplicationEntity> applicantentities, LicenceStateEnum application, List<String> decisions,
			boolean b);

	@Query("SELECT e FROM Licence e WHERE e.applicantEntity.referingEntityId=?1 AND e.licenseState = ?2 ORDER BY e.createdAt DESC")
	List<Licence> findByReferenceLicenceIdAndLicenseState(Long referingEntityId, LicenceStateEnum active);

	@Query("SELECT e FROM Licence e WHERE e.applicantEntity.referingEntityId=?1 AND e.licenseState = ?2 AND e.decision IN ?3 AND e.isDraft =?4 ORDER BY e.createdAt DESC")
	List<Licence> findByReferenceLicenceIdAndLicenseStateAndDecisionNotInAndIsDraft(Long referingEntityId,
			LicenceStateEnum application, List<String> decisions, boolean b);

	List<Licence> findByIdInAndLicenseState(List<Long> licenceids, LicenceStateEnum active);

	List<Licence> findByIdInAndLicenseStateAndDecisionNotInAndIsDraft(List<Long> licenceids,
			LicenceStateEnum application, List<String> decisions, boolean b);

}
