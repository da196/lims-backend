/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.uaa.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import tz.go.tcra.lims.task.dto.ActorMinDto;
import tz.go.tcra.lims.uaa.dto.ApplicantMinDto;
import tz.go.tcra.lims.uaa.dto.UserMaxDto;
import tz.go.tcra.lims.uaa.entity.LimsUser;

/**
 *
 * @author emmanuel.mfikwa
 */
@Repository
public interface UserRepository extends JpaRepository<LimsUser, Long> {

	LimsUser findByEmail(String email);

	List<LimsUser> findByStatus(boolean status);

	@Query("SELECT e FROM LimsUser e " + "INNER JOIN UserRole e2 ON e.id=e2.userID " + "WHERE e2.roleID !=2")
	List<LimsUser> findInternalUsers();

	@Query("SELECT new tz.go.tcra.lims.uaa.dto.UserMaxDto(" + "e.id," + "e.firstName," + "e.middleName," + "e.lastName,"
			+ "e.phone," + "e.email," + "e.status," + "e3.name) FROM LimsUser e "
			+ "RIGHT OUTER JOIN UserRole e2 ON e.id=e2.userID " + "RIGHT OUTER JOIN Role e3 ON e2.roleID=e3.id ")
	Page<UserMaxDto> findAllUsers(Pageable page);

	@Query("SELECT new tz.go.tcra.lims.uaa.dto.UserMaxDto(" + "e.id," + "e.firstName," + "e.middleName," + "e.lastName,"
			+ "e.phone," + "e.email," + "e.status," + "e3.name) FROM LimsUser e " + ""
			+ "RIGHT OUTER JOIN UserRole e2 ON e.id=e2.userID " + "RIGHT OUTER JOIN Role e3 ON e2.roleID=e3.id "
			+ "WHERE CONCAT(e.firstName,e.middleName,e.lastName,e.phone,e.email,e.gender,e3.name) LIKE %?1%")
	Page<UserMaxDto> findAllUsersByKeyword(String keyword, Pageable page);

	@Query("SELECT new tz.go.tcra.lims.uaa.dto.ApplicantMinDto(e.firstName,e.middleName,e.lastName,e.phone,e.email,e.physicalAddress,e.postalAddress) FROM LimsUser e "
			+ "INNER JOIN UserRole e2 ON e.id=e2.userID " + "WHERE  e2.roleID =2 " + "ORDER BY e.createdAt DESC")
	Page<ApplicantMinDto> findApplicantUsers(Pageable page);

	@Query("SELECT new tz.go.tcra.lims.task.dto.ActorMinDto(e.id,e.firstName,e.middleName,e.lastName) "
			+ "FROM LimsUser e INNER JOIN UserRole e2 ON e.id=e2.userID WHERE e2.roleID=?1")
	List<ActorMinDto> findActorsByRole(Long roleId);

	@Query("SELECT e FROM LimsUser e INNER JOIN UserRole e2 ON e.id=e2.userID WHERE e2.roleID=?1")
	List<LimsUser> findUsersByRole(Long roleId);

	@Query("SELECT new tz.go.tcra.lims.task.dto.ActorMinDto(e.id,e.firstName,e.middleName,e.lastName) FROM LimsUser e WHERE e.id=?1")
	Optional<ActorMinDto> findActorById(Long id);

	@Query("SELECT new tz.go.tcra.lims.uaa.dto.ApplicantMinDto(e.firstName,e.middleName,e.lastName,e.phone,e.email,e.physicalAddress,e.postalAddress) FROM LimsUser e "
			+ "INNER JOIN UserRole e2 ON e.id=e2.userID " + "WHERE   e2.roleID =2 " + "ORDER BY e.createdAt DESC")
	Page<ApplicantMinDto> findApplicantUsersByStatus(boolean b, Pageable pageable);

	@Query("SELECT new tz.go.tcra.lims.uaa.dto.ApplicantMinDto(e.firstName,e.middleName,e.lastName,e.phone,e.email,e.physicalAddress,e.postalAddress) FROM LimsUser e "
			+ "INNER JOIN UserRole e2 ON e.id=e2.userID "
			+ "WHERE CONCAT(e.firstName,e.middleName,e.lastName,e.phone,e.email,e.gender) LIKE %?1% AND e2.roleID =2 "
			+ "ORDER BY e.createdAt DESC")
	Page<ApplicantMinDto> findApplicantUsersByKeyword(String keyword, Pageable pageable);

	@Query("SELECT new tz.go.tcra.lims.uaa.dto.ApplicantMinDto(" + "e.firstName," + "e.middleName," + "e.lastName,"
			+ "e.phone," + "e.email," + "e.physicalAddress," + "e.postalAddress) " + "FROM LimsUser e WHERE e.id =?1")
	ApplicantMinDto findApplicantMinDtoById(Long id);

	@Query("SELECT e FROM LimsUser e " + "INNER JOIN UserRole e2 ON e.id=e2.userID " + "WHERE e2.roleID !=2")
	List<LimsUser> findInternalUsers(Pageable pageable);

}
