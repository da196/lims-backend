package tz.go.tcra.lims.licence.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import tz.go.tcra.lims.entity.LicenceCategoryServiceDetail;
import tz.go.tcra.lims.entity.LicenceCategory;
import tz.go.tcra.lims.product.entity.LicenceProduct;
import tz.go.tcra.lims.entity.LicenceServiceDetail;
import tz.go.tcra.lims.licence.dto.LicenseCategoryDto;
import tz.go.tcra.lims.miscellaneous.enums.LicenceCategoryFlagEnum;
import tz.go.tcra.lims.licence.dto.LicenseCategoryMaxDto;
import tz.go.tcra.lims.licence.dto.LicenseCategoryMinDto;
import tz.go.tcra.lims.licence.dto.LicenseDetailMaxDto;
import tz.go.tcra.lims.licence.repository.LicenceCategoryServiceRepository;
import tz.go.tcra.lims.uaa.entity.LimsUser;
import tz.go.tcra.lims.utils.AppUtility;
import tz.go.tcra.lims.utils.Response;
import tz.go.tcra.lims.utils.ResponseCode;
import tz.go.tcra.lims.utils.exception.DataNotFoundException;
import tz.go.tcra.lims.utils.exception.GeneralException;
import tz.go.tcra.lims.licence.repository.LicenceCategoryRepository;
import tz.go.tcra.lims.licence.repository.LicenceDetailRepository;

/**
 * @author DonaldSj
 */
@Service
@Slf4j
public class LicenseCategoryServiceImpl implements LicenseCategoryService {

    @Autowired
    private LicenceCategoryServiceRepository categoryServiceRepo;
    
    @Autowired
    private LicenceDetailRepository licenceDetailRepo;
    
    @Autowired
    private LicenceCategoryRepository licenseCategoryRepository;

    @Autowired
    private LicenseCategoryAssembler assembler;

    @Autowired
    private AppUtility utility;
    
    @Override
    @Transactional
    public Response<EntityModel<LicenceCategory>> saveLicenseCategory(LicenseCategoryDto data,Long id) {
        Response<EntityModel<LicenceCategory>> response=new Response<>(ResponseCode.SUCCESS,true,"LICENCSE CATEGORY SAVED SUCCESSFULLY",null);
        try {
            LimsUser actor=utility.getUser();
            
            LicenceCategoryFlagEnum flag=data.getFlag();
            LicenceCategory licenseCategory = new LicenceCategory();
            if(id > 0L){

                licenseCategory=licenseCategoryRepository.getOne(id);
                licenseCategory.setUpdatedAt(LocalDateTime.now());
                licenseCategory.setUpdatedBy(actor);
            }else{
            
                licenseCategory.setCreatedBy(actor);
            }

            licenseCategory.setName(data.getName());
            licenseCategory.setCode(data.getCode());
            licenseCategory.setDisplayName(data.getDisplayName());
            licenseCategory.setDescription(data.getDescription());

            if(data.getParentId() > 0L){
                
                licenseCategory.setParent(data.getParentId());
                if(data.getServices().size() > 0){
                    
                    licenseCategory.setHasService(Boolean.TRUE);
                }
            }
            
            licenseCategory.setFlag(flag);
            licenseCategory=licenseCategoryRepository.saveAndFlush(licenseCategory);
            
            //save services
            categoryServiceRepo.deleteByCategory(licenseCategory);
            for(Long srv:data.getServices()){
                
                LicenceServiceDetail service=licenceDetailRepo.getOne(srv);
                LicenceCategoryServiceDetail categoryService=new LicenceCategoryServiceDetail();
                categoryService.setService(service);
                categoryService.setCategory(licenseCategory);
                
                categoryServiceRepo.saveAndFlush(categoryService);
            }
                    
            //save audit
            String activity=id > 0?"NEW Licence Category [ "+licenseCategory.getName()+"] Save ":"Licence Category [ "+licenseCategory.getName()+"] Updated";
            utility.saveAudit(activity, actor);
            
            response.setData(assembler.toModel(licenseCategory));

        } catch (EntityNotFoundException e) {

            log.error(e.getMessage());
            e.printStackTrace();
            throw new DataNotFoundException("LICENCE CATEGORY NOT FOUND");
        }catch (Exception e) {

                log.error(e.getMessage());
                e.printStackTrace();
                throw new GeneralException("INTERNAL SERVER ERROR");
        }

        return response;
    }

    @Override
    public Response<EntityModel<LicenceCategory>> activateDeactivateLicenseCategoryById(Long id) {
        Response<EntityModel<LicenceCategory>> response=new Response<>(ResponseCode.SUCCESS,true,"LICENCSE CATEGORY ACTIVATED/DEACTIVATED SUCCESSFULLY",null);
        try {
                LimsUser actor=utility.getUser();
                LicenceCategory data = licenseCategoryRepository.getOne(id);
                Boolean active=data.getActive()?false:true;
                
                data.setActive(active);
                data.setUpdatedAt(LocalDateTime.now());
                data.setUpdatedBy(actor);                
                data=licenseCategoryRepository.saveAndFlush(data);
                
                //save audit
                String activity=active?"Licence Category [ "+data.getName()+" ] Activated":"Licence Category [ "+data.getName()+"] De-Activated";
                utility.saveAudit(activity, actor);
                
                response.setData(assembler.toModel(data));
                
        } catch (EntityNotFoundException e) {

            log.error(e.getMessage());
            e.printStackTrace();
            throw new DataNotFoundException("LICENCE CATEGORY NOT FOUND");
            
        }catch (Exception e) {
            
            log.error(e.getMessage());
            e.printStackTrace();
            throw new GeneralException("INTERNAL SERVER ERROR");
        }
        
        return response;
    }
    
    @Override
    public Response<EntityModel<LicenceCategory>> findLicenseCategoryById(Long id) {
        Response<EntityModel<LicenceCategory>> response=new Response<>(ResponseCode.SUCCESS,true,"LICENCSE CATEGORY RETRIEVED",null);
        try {
            
            LimsUser actor=utility.getUser();
            utility.saveAudit("Attempting to view licence category [ "+id+"]", actor);
            
            Optional<LicenceCategory> existing=licenseCategoryRepository.findById(id);
            
            if(!existing.isPresent()){
                
                utility.saveAudit("Licence category [ "+id+"] not found", actor);
                response.setMessage("LICENCSE CATEGORY NOT FOUND");
                return response;
            }
            
            LicenceCategory data=existing.get();
            
            utility.saveAudit("Licence category [ "+id+"] viewed", actor);
            
            response.setData(assembler.toModel(data));
            
        } catch (EntityNotFoundException e) {
            
            response.setMessage("LICENCSE CATEGORY NOT FOUND");
            
        }catch (Exception e) {
            
            log.error(e.getMessage());
            e.printStackTrace();
            throw new GeneralException("INTERNAL SERVER ERROR");
        }
        
        return response;
    }

    @Override
    public Response<CollectionModel<EntityModel<LicenceCategory>>> getListOfLicenseCategory() {
        Response<CollectionModel<EntityModel<LicenceCategory>>> response=new Response<>(ResponseCode.SUCCESS,true,"LICENCE CATEGORIES RETRIEVED SUCCESSFULLY",null);
        try {
            List<LicenceCategory> data = licenseCategoryRepository.findAll();

            if(data.size() == 0){

                response.setMessage("LICENCE CATEGORIES NOT FOUND");
                return response;
            }

            response.setData(assembler.toCollectionModel(data));

        } catch (Exception e) {

            log.error(e.getMessage());
            e.printStackTrace();
            throw new GeneralException("INTERNAL SERVER ERROR");
        }

        return response;
    }

    @Override
    public Response<List<LicenseCategoryMinDto>> getLicenseCategoriesMin() {
        Response<List<LicenseCategoryMinDto>> response=new Response<>(ResponseCode.SUCCESS,true,"LICENCE CATEGORIES RETRIEVED SUCCESSFULLY",null);
        try {
            List<LicenseCategoryMinDto> data = licenseCategoryRepository.findAllWithoutFlagMinAndActive(LicenceCategoryFlagEnum.LEAF,true);

            if(data.size() == 0){

                response.setMessage("LICENCE CATEGORIES NOT FOUND");
                return response;
            }

            response.setData(data);

        } catch (Exception e) {

            log.error(e.getMessage());
            e.printStackTrace();
            throw new GeneralException("INTERNAL SERVER ERROR");
        }

        return response;
    }
    
    @Override
    public Response<List<LicenseCategoryMaxDto>> getListOfLicenseCategories(Long parentId,String flag) {
        Response<List<LicenseCategoryMaxDto>> response=new Response<>(ResponseCode.SUCCESS,true,"LICENCE CATEGORIES RETRIEVED SUCCESSFULLY",null);
        try {
            List<LicenceCategory> categories=new ArrayList();
            if(parentId > 0L){
            
                categories = licenseCategoryRepository.findAllByParentAndActive(parentId,true);   
            }else{
                
                categories=licenseCategoryRepository.findByFlagAndActive(LicenceCategoryFlagEnum.valueOf(flag.toUpperCase()),true);                
            }         
            
            if(categories.isEmpty()){
            
                response.setMessage("LICENCE CATEGORIES NOT FOUND");
                return response;
            }
            
            List<LicenseCategoryMaxDto> data=new ArrayList();
            for(LicenceCategory category :categories){
                LicenseCategoryMaxDto dt=new LicenseCategoryMaxDto();
                dt.setId(category.getId());
                dt.setCode(category.getCode());
                dt.setName(category.getName());
                dt.setDisplayName(category.getDisplayName());
                dt.setDescription(category.getDescription());
                dt.setParentId(category.getParent());
                dt.setActive(category.getActive());
                dt.setFlag(category.getFlag());
                
                List<LicenseDetailMaxDto> services=new ArrayList();
                for(LicenceCategoryServiceDetail service: category.getServices()){

                    LicenseDetailMaxDto srv=new LicenseDetailMaxDto();
                    srv.setId(service.getService().getId());
                    srv.setCode(service.getService().getCode());
                    srv.setName(service.getService().getName());

                    services.add(srv);
                }
                dt.setServices(services);
                
                data.add(dt);
                
            }
            
            response.setData(data);
            
        } catch (IllegalArgumentException e) {
            
            log.error(e.getMessage());
            e.printStackTrace();
            response.setMessage("LICENCE CATEGORIES NOT FOUND");
            
        } catch (Exception e) {
            
            log.error(e.getMessage());
            e.printStackTrace();
            throw new GeneralException("INTERNAL SERVER ERROR");
        }
        
        return response;
    }

    @Override
    public Response<CollectionModel<EntityModel<LicenceCategory>>> getListOfLicenseCategoryByFlag(String flag) {
        Response<CollectionModel<EntityModel<LicenceCategory>>> response=new Response<>(ResponseCode.SUCCESS,true,"FLAGGED LICENCE CATEGORIES RETRIEVED SUCCESSFULLY",null);
        try{
            
            List<LicenceCategory> data=new ArrayList();
            if(flag.compareToIgnoreCase(LicenceCategoryFlagEnum.LEAF.toString()) == 0){
            
                data=licenseCategoryRepository.findByFlagAndActive(LicenceCategoryFlagEnum.LEAF,true);
            }else{
                
                data=licenseCategoryRepository.findByFlagAndActive(LicenceCategoryFlagEnum.ROOT,true);
            }
             
            
            if(data.size() == 0){
            
                response.setMessage("FLAGGED LICENCE CATEGORIES NOT FOUND");
                return response;
            }
            
            response.setData(assembler.toCollectionModel(data));
            
        }catch(Exception e){
        
            log.error(e.getMessage());
            e.printStackTrace();
            throw new GeneralException("INTERNAL SERVER ERROR");
        }
        return response; 
    }

    @Override
    public List<LicenceCategory> getListOfLicenceCategoryTopHierachyByCategoryId(Long id) {
        List<LicenceCategory> response=new ArrayList<LicenceCategory>();        
        try{        
            Optional<LicenceCategory> category=licenseCategoryRepository.findById(id);
            
            if(!category.isPresent()){
            
                return response;
            }
            
            response.add(category.get());
            
            Optional<LicenceCategory> parent=null;
            Long parentId=category.get().getParent();
            while(true){
                
                parent=licenseCategoryRepository.findById(parentId);
            
                if(!parent.isPresent()){

                    break;
                } 
                
                response.add(parent.get());
                
                if(parent.get().getParent() == null){
            
                    break;
                }
                
                parentId=parent.get().getParent();
            }
            
            
        }catch(Exception e){
        
            log.error(e.getLocalizedMessage());
        }
        
        return response;
    }

    @Override
    public LicenseCategoryMinDto composeLicenseCategoryMinDto(LicenceCategory data) {
        LicenseCategoryMinDto response=new LicenseCategoryMinDto();
        try{
        
            response.setId(data.getId());
            response.setCode(data.getCode());
            response.setDisplayName(data.getDisplayName());
            response.setCategoryName(data.getName());
            
        }catch(Exception e){
        
            log.error(e.getLocalizedMessage());
            e.printStackTrace();
        }
        
        return response;
    }

    @Override
    public Long getRootLicenceCategoryByProduct(LicenceProduct data) throws DataNotFoundException,Exception {
        Long parentId=data.getLicenseCategory().getParent();
        Long rootId=data.getLicenseCategory().getId();
        while(true){

            if(parentId == null){

                return rootId;
            }

            Optional<LicenceCategory> parent=licenseCategoryRepository.findById(parentId);

            if(!parent.isPresent()){

                throw new DataNotFoundException("PARENT LICENCE CATEGORY NOT FOUND "+parentId);
            }

            parentId=parent.get().getParent();
            rootId=parent.get().getId();
        }
    }

    @Override
    public LicenceCategory getLevel1LicenceCategoryByProduct(LicenceProduct data) throws DataNotFoundException, Exception {
        Long parentId=data.getLicenseCategory().getParent();
        LicenceCategory category=data.getLicenseCategory();
        LicenceCategory current_category=null;
        while(true){

            if(parentId == null){

                return category;
            }
            
            category=current_category;
            Optional<LicenceCategory> parent=licenseCategoryRepository.findById(parentId);

            if(!parent.isPresent()){

                throw new DataNotFoundException("PARENT LICENCE CATEGORY NOT FOUND "+parentId);
            }

            parentId=parent.get().getParent();
            current_category=parent.get();
        }
    }
}
