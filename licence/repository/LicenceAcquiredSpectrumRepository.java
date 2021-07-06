/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.licence.repository;

import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tz.go.tcra.lims.entity.LicenceAcquiredSpectrum;
import tz.go.tcra.lims.licence.dto.SpectrumValueDto;

/**
 *
 * @author emmanuel.mfikwa
 */
@Repository
public interface LicenceAcquiredSpectrumRepository extends JpaRepository<LicenceAcquiredSpectrum,Long>{
    
    LicenceAcquiredSpectrum findFirstByLicenceIdAndLowerBandAndUpperBand(Long licenceId,Double lowerBand,Double upperBand);
    
    @Query("SELECT new tz.go.tcra.lims.licence.dto.SpectrumValueDto(e.lowerBand,e.upperBand) FROM LicenceAcquiredSpectrum e WHERE e.licenceId=?1")
    List<SpectrumValueDto> findSpectrumValueByLicenceId(Long licenceId);
    
    @Transactional
    @Modifying
    @Query("UPDATE LicenceAcquiredSpectrum e SET e.active=false WHERE e.licenceId=?1")
    void deactivateAcquiredSpectrum(Long licenceId);
}
