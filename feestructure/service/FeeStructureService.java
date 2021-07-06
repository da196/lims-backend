package tz.go.tcra.lims.feestructure.service;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;
import tz.go.tcra.lims.feestructure.dto.FeeDto;

import tz.go.tcra.lims.feestructure.entity.FeeStructure;
import tz.go.tcra.lims.feestructure.dto.FeeStructureMinDto;
import tz.go.tcra.lims.feestructure.entity.Feeable;
import tz.go.tcra.lims.miscellaneous.enums.FeeableTypeEnum;
import tz.go.tcra.lims.portal.application.dto.PayableFeesDto;
import tz.go.tcra.lims.utils.Response;
import tz.go.tcra.lims.utils.exception.DataNotFoundException;

/**
 * @author DonaldSj
 */
@Service
public interface FeeStructureService {

    Response<EntityModel<FeeStructure>> findFeeStructureById(Long feeId);

    Response<CollectionModel<EntityModel<FeeStructure>>> getListOfAllFeeStructures(String keyword,
			Pageable pageable);
    
    FeeStructureMinDto composeFeeStructureMinDto(FeeStructure data) throws Exception;
    
    List<PayableFeesDto> findFeeStructureByFeeable(Feeable feeable);
    
    Response<EntityModel<FeeStructure>> saveFeeStructure(FeeDto data,Long id);
    
    Feeable getFeable(Long feableId,FeeableTypeEnum type) throws DataNotFoundException,Exception;
    
    Response<EntityModel<FeeStructure>> activateDeactivateFeeStructureById(Long id);
    
    Response<List<FeeStructure>> getListOfAllFeeStructuresByFeableType(FeeableTypeEnum feableType);
}
