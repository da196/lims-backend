package tz.go.tcra.lims.portal.application.services;

import java.util.List;
import tz.go.tcra.lims.licencee.dto.NameChangeDto;
import tz.go.tcra.lims.licencee.dto.ShareholderChangeDto;
import tz.go.tcra.lims.miscellaneous.enums.EntityApplicationTypeEnum;
import tz.go.tcra.lims.portal.application.dto.EntityApplicationDto;
import tz.go.tcra.lims.portal.application.dto.TaskActionPortalDto;
import tz.go.tcra.lims.portal.application.dto.EntityApplicationMaxPortalDto;
import tz.go.tcra.lims.portal.application.dto.EntityApplicationMinPortalDto;
import tz.go.tcra.lims.portal.application.dto.LicenceeEntityMinMinPortalDto;
import tz.go.tcra.lims.portal.application.dto.LicenceeEntityMinPortalDto;
import tz.go.tcra.lims.utils.Response;
import tz.go.tcra.lims.utils.SaveResponseDto;
import tz.go.tcra.lims.utils.exception.DataNotFoundException;

/**
 * @author DonaldSj
 */

public interface EntityApplicationPortalService {

    Response<EntityApplicationMaxPortalDto> saveEntityApplication(EntityApplicationDto data,Long id,EntityApplicationTypeEnum applicationType);

    Response<EntityApplicationMaxPortalDto> findEntityApplicationById(Long id);

    Response<List<EntityApplicationMinPortalDto>> getEntityApplicationByApplicant();

    Response<List<LicenceeEntityMinMinPortalDto>> getListOfEntities();

    Response<LicenceeEntityMinPortalDto> findLicenceeById(Long id);

    Response<List<EntityApplicationMinPortalDto>> getPendingCertificateOfRegistraEntityApplicationsByApplicant();

    Response<SaveResponseDto> savePortalActivity(TaskActionPortalDto data,Long id);
    
//    Response<EntityApplicationMaxPortalDto> saveChangeOfName(EntityApplicationDto data,Long id);
    
    ShareholderChangeDto getShareholderChangeDetailsByApplicationId(Long applicationId);
    
    NameChangeDto getNameChangeByApplicationId(Long applicationId);
    
//    String getWorkflowTypeCode(EntityApplicationTypeEnum applicationType);
    
    void saveShareholderChangeDetails(ShareholderChangeDto data,Long applicationId) throws DataNotFoundException,Exception;
    
    void saveNameChangeDetails(NameChangeDto data,Long applicationId) throws DataNotFoundException,Exception;
}
