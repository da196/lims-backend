/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.miscellaneous.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tz.go.tcra.lims.miscellaneous.entity.ConsultationLetterConfig;
import tz.go.tcra.lims.miscellaneous.enums.MinistryEnum;

/**
 *
 * @author emmanuel.mfikwa
 */
@Repository
public interface ConsultationLetterConfigRepository extends JpaRepository<ConsultationLetterConfig,Long>{
    
    Optional<ConsultationLetterConfig> findByFlag(MinistryEnum flag);
}
