/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.miscellaneous.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;
import tz.go.tcra.lims.miscellaneous.entity.Form;
import tz.go.tcra.lims.miscellaneous.entity.FormItem;
import tz.go.tcra.lims.miscellaneous.dto.FormItemMinDto;
import tz.go.tcra.lims.miscellaneous.dto.FormDto;
import tz.go.tcra.lims.miscellaneous.dto.FormItemDto;
import tz.go.tcra.lims.miscellaneous.dto.FormItemMaxDto;
import tz.go.tcra.lims.miscellaneous.dto.FormMaxDto;
import tz.go.tcra.lims.miscellaneous.entity.FormItemOption;
import tz.go.tcra.lims.miscellaneous.enums.FormFeedbackTypeEnum;
import tz.go.tcra.lims.miscellaneous.enums.FormFlagEnum;
import tz.go.tcra.lims.miscellaneous.repository.FormItemOptionRepository;
import tz.go.tcra.lims.uaa.entity.LimsUser;
import tz.go.tcra.lims.utils.AppUtility;
import tz.go.tcra.lims.utils.Response;
import tz.go.tcra.lims.utils.ResponseCode;
import tz.go.tcra.lims.utils.exception.DataNotFoundException;
import tz.go.tcra.lims.utils.exception.GeneralException;
import tz.go.tcra.lims.miscellaneous.repository.FormItemRepository;
import tz.go.tcra.lims.miscellaneous.repository.FormRepository;

/**
 *
 * @author emmanuel.mfikwa
 */
@Service
@Slf4j
public class FormServiceImpl implements FormService{

    @Autowired
    private FormItemRepository formItemRepo;
    
    @Autowired
    private FormItemOptionRepository formItemOptionRepo;
    
    @Autowired
    private FormRepository formRepo;
    
    @Autowired
    private FormAssembler assembler;
    
    @Autowired
    private AppUtility utility;
    
    @Override
    public Response<EntityModel<Form>> saveForm(FormDto data, Long id) {
        Response<EntityModel<Form>> response=new Response<>(ResponseCode.SUCCESS,true,"FORM SAVED SUCCESSFULLY",null);
        try{
        
            LimsUser actor=utility.getUser();
            Form form=new Form();
            
            if(id > 0){
                
                Optional<Form> existing=formRepo.findById(id);
                
                if(!existing.isPresent()){
                
                    throw new DataNotFoundException("FORM NOT FOUND");
                }
                
                form=existing.get();
                form.setUpdatedAt(LocalDateTime.now());
                form.setUpdatedBy(actor);
            }else{
            
                form.setCreatedBy(actor);
            }
            
            form.setFormType(data.getType());
            form.setName(data.getName());
            form.setCode(data.getCode());
            form.setDescription(data.getDescription());
            form=formRepo.save(form);
            
            response.setData(assembler.toModel(form));
            
        }catch(DataNotFoundException e){
        
            log.error(e.getLocalizedMessage());
            throw new DataNotFoundException(e.getLocalizedMessage());
            
        }catch(Exception e){
        
            log.error(e.getLocalizedMessage());
            e.printStackTrace();
            throw new GeneralException("INTERNAL SERVER ERROR");
        }
        
        return response;
    }

    @Override
    public Response<CollectionModel<EntityModel<Form>>> listForms() {
        Response<CollectionModel<EntityModel<Form>>> response=new Response<>(ResponseCode.SUCCESS,true,"LICENCE FORMS RETRIEVED SUCCESSFULLY",null);
        try{
        
            List<Form> forms=formRepo.findByActive(true);
            
            if(forms.size() == 0){
            
                response.setMessage("FORMS NOT FOUND");
                return response;
            }
            
            response.setData(assembler.toCollectionModel(forms));
            
        }catch(Exception e){
        
            e.printStackTrace();
            throw new GeneralException("INTERNAL SERVER ERROR");
        }
        
        return response;
    }

    @Override
    public Response<CollectionModel<EntityModel<Form>>> listAllForms() {
        Response<CollectionModel<EntityModel<Form>>> response=new Response<>(ResponseCode.SUCCESS,true,"LICENCE FORMS RETRIEVED SUCCESSFULLY",null);
        try{
        
            List<Form> forms=formRepo.findAll();
            
            if(forms.size() == 0){
            
                response.setMessage("LICENCE FORMS NOT FOUND");
                return response;
            }
            
            response.setData(assembler.toCollectionModel(forms));
            
        }catch(Exception e){
        
            e.printStackTrace();
            throw new GeneralException("INTERNAL SERVER ERROR");
        }
        
        return response;
    }

    @Override
    public Response<FormMaxDto> getFormDetails(Long id) {
        Response<FormMaxDto> response=new Response<>(ResponseCode.SUCCESS,true,"LICENCE FORM DETAILS RETRIEVED SUCCESSFULLY",null);
        try{
        
            Optional<Form> existing=formRepo.findById(id);            
            if(!existing.isPresent()){
            
                response.setMessage("LICENCE FORM NOT FOUND");
                return response;
            }
            Form fm=existing.get();
            
            FormMaxDto form=new FormMaxDto();
            form.setFormType(fm.getFormType());
            form.setId(fm.getId());
            form.setCode(fm.getCode());
            form.setName(fm.getName());
            form.setDescription(fm.getDescription());
            form.setItems(this.getFormItemsMinDto(fm.getId()));
            
            response.setData(form);
        }catch(Exception e){
        
            e.printStackTrace();
            throw new GeneralException("INTERNAL SERVER ERROR");
        }
        
        return response;
    }

    @Override
    public Response<EntityModel<Form>> activateDeactivateForm(Long id) {
        Response<EntityModel<Form>> response=new Response<>(ResponseCode.SUCCESS,true,"LICENSE FORM UPDATED SUCCESSFULLY",null);
        try{        
            LimsUser actor=utility.getUser();
            
            Optional<Form> existing=formRepo.findById(id);            
            if(!existing.isPresent()){
            
                throw new DataNotFoundException("LICENCE FORM NOT FOUND ["+id+"]");
            }
            
            Form form=existing.get();
            Boolean active=form.getActive()?false:true;
            
            form.setActive(active);
            form.setUpdatedBy(actor);
            form.setUpdatedAt(LocalDateTime.now());
            form=formRepo.saveAndFlush(form);
            
            response.setData(assembler.toModel(form));
            
        }catch(DataNotFoundException e){
        
            log.error(e.getMessage());
            throw new DataNotFoundException(e.getMessage());
        }catch(Exception e){
        
            log.error(e.getMessage());
            e.printStackTrace();
            throw new GeneralException("INTERNAL SERVER ERROR");
        }
        
        return response;
    }

    @Override
    public List<FormItemMinDto> getFormItemsMinDto(Long formId) {
        List<FormItemMinDto> response=new ArrayList();
        try{
        
            //get all form sections
            List<FormItem> sections=formItemRepo.findByFormIdAndFlag(formId,FormFlagEnum.SECTION);
            
            for(FormItem section : sections){
                
                FormItemMinDto sectionItem=new FormItemMinDto();
                sectionItem.setId(section.getId());
                sectionItem.setName(section.getName());
                sectionItem.setDisplayName(section.getDisplayName());
                sectionItem.setFlag(section.getFlag());
                
                List<FormItemMinDto> level1Items=new ArrayList();
                
                //get all level1s
                List<FormItem> level1s=formItemRepo.findByParent(section.getId());
                for(FormItem level1 : level1s){
                
                    FormItemMinDto level1Item=new FormItemMinDto();
                    level1Item.setId(level1.getId());
                    level1Item.setName(level1.getName());
                    level1Item.setDisplayName(level1.getDisplayName());
                    level1Item.setFlag(level1.getFlag());
                    level1Item.setFeedbackType(level1.getFeedbackType());
                    if(level1.getFeedbackType() == FormFeedbackTypeEnum.FIXED){
                    
                        level1Item.setOptions(formItemOptionRepo.findByFormItemId(level1.getId(), Boolean.TRUE));
                    }
                    
                    //get all the level2s
                    List<FormItemMinDto> level2Items=new ArrayList();
                    List<FormItem> level2s=formItemRepo.findByParent(level1.getId());
                    for(FormItem level2 : level2s){
                    
                        FormItemMinDto level2Item=new FormItemMinDto();
                        level2Item.setId(level2.getId());
                        level2Item.setName(level2.getName());
                        level2Item.setDisplayName(level2.getDisplayName());
                        level2Item.setFlag(level2.getFlag());
                        level2Item.setFeedbackType(level2.getFeedbackType());
                        if(level2.getFeedbackType() == FormFeedbackTypeEnum.FIXED){
                    
                            level2Item.setOptions(formItemOptionRepo.findByFormItemId(level2.getId(), Boolean.TRUE));
                        }
                        
                        //get all the level3s
                        List<FormItemMinDto> level3Items=new ArrayList();
                        List<FormItem> level3s=formItemRepo.findByParent(level2.getId());
                        for(FormItem level3 : level3s){
                        
                            FormItemMinDto level3Item=new FormItemMinDto();
                            level3Item.setId(level3.getId());
                            level3Item.setName(level3.getName());
                            level3Item.setDisplayName(level3.getDisplayName());
                            level3Item.setFlag(level3.getFlag());
                            level3Item.setFeedbackType(level3.getFeedbackType());
                            if(level3.getFeedbackType() == FormFeedbackTypeEnum.FIXED){
                    
                                level3Item.setOptions(formItemOptionRepo.findByFormItemId(level3.getId(), Boolean.TRUE));
                            }
                            level3Items.add(level3Item);
                        }
                        level2Item.setItems(level3Items);
                        
                        level2Items.add(level2Item);
                    }
                    
                    level1Item.setItems(level2Items);
                    
                    level1Items.add(level1Item);
                }
                
                sectionItem.setItems(level1Items);
                
                response.add(sectionItem);
            }
        }catch(Exception e){

            log.error(e.getLocalizedMessage());
            e.printStackTrace();
        }
        
        return response;
    }

    @Override
    public Response<List<FormItemMinDto>> getFormItems(Long formId) {
        Response<List<FormItemMinDto>> response=new Response(ResponseCode.SUCCESS,true,"FORM ITEMS RETRIEVED SUCCESSFULLY",null);
        try{
        
            List<FormItemMinDto> data=new ArrayList();
            List<FormItem> items=formItemRepo.findByFormIdAndNonParameter(formId,FormFlagEnum.PARAMETER);
            
            if(items.size() == 0){
            
                response.setMessage("FORM ITEMS NOT FOUND");
                return response;
            }
            
            for(FormItem item : items){
                
                FormItemMinDto formItem=new FormItemMinDto();
                formItem.setId(item.getId());
                formItem.setName(item.getName());
                formItem.setDisplayName(item.getDisplayName());
                formItem.setFlag(item.getFlag());
                formItem.setFeedbackType(item.getFeedbackType());
                if(item.getFeedbackType() == FormFeedbackTypeEnum.FIXED){
                    
                    formItem.setOptions(formItemOptionRepo.findByFormItemId(item.getId(), Boolean.TRUE));
                }
                data.add(formItem);
            }
            
            response.setData(data);
        }catch(Exception e){

            log.error(e.getLocalizedMessage());
            e.printStackTrace();
        }
        
        return response;
    }
    
    @Override
    @Transactional
    public Response<List<FormItem>> saveFormItems(List<FormItemDto> data, Long id) {
        Response<List<FormItem>> response=new Response<>(ResponseCode.SUCCESS,true,"FORM ITEMS SAVED SUCCESSFULLY",null);
        try{
            LimsUser actor=utility.getUser();
            
            Optional<Form> existingForm=formRepo.findById(id);
                
            if(!existingForm.isPresent()){

                throw new DataNotFoundException("FORM NOT FOUND");
            }
            
            Form form=existingForm.get();
            for(FormItemDto item : data){
                
                FormItem formItem=new FormItem();
                
                if(item.getId() > 0){
                
                    Optional<FormItem> existing=formItemRepo.findById(item.getId());
                    
                    if(!existing.isPresent()){
                    
                        throw new DataNotFoundException("FORM ITEM NOT FOUND ["+item.getId()+"]");
                    }
                    
                    formItem=existing.get();
                    formItem.setUpdatedAt(LocalDateTime.now());
                    formItem.setUpdatedBy(actor);
                }else{
                
                    formItem.setCreatedBy(actor);
                }
                
                formItem.setFeedbackType(item.getFeedbackType());
                formItem.setName(item.getName());
                formItem.setDisplayName(item.getDisplayName());
                formItem.setParent(item.getParent());
                formItem.setFormId(form.getId());
                formItem.setFlag(item.getFlag());
               
                formItem=formItemRepo.saveAndFlush(formItem);
                if(item.getFeedbackType() == FormFeedbackTypeEnum.FIXED){
                    
                    if(item.getOptions().size() == 0){
                    
                        throw new DataNotFoundException("FEEDBACK OPTIONS FOR ITEM MISSING");
                    }
                    
                    //save options
                    if(item.getId() > 0){
                        
                        formItemOptionRepo.deactivateFormItemOption(item.getId());
                    }
                    
                    for(String option : item.getOptions()){
                    
                        FormItemOption opt=new FormItemOption();
                        
                        Optional<FormItemOption> existingItemOption=formItemOptionRepo.findByFormItemIdAndName(formItem.getId(), option);
                        if(existingItemOption.isPresent()){
                            
                            opt=existingItemOption.get();
                            opt.setUpdatedAt(LocalDateTime.now());
                            opt.setActive(Boolean.TRUE);
                        }
                        
                        opt.setFormItemId(formItem.getId());
                        opt.setName(option);
                       
                        formItemOptionRepo.save(opt);
                    }                
                }
            }
            
            response.setData(formItemRepo.findByFormIdAndActive(id, Boolean.TRUE));
        }catch(DataNotFoundException e){
        
            log.error(e.getLocalizedMessage());
            throw new DataNotFoundException(e.getLocalizedMessage());
            
        }catch(Exception e){
        
            log.error(e.getLocalizedMessage());
            e.printStackTrace();
            throw new GeneralException("INTERNAL SERVER ERROR");
        }
        
        return response;
    }

    @Override
    public Response<FormItemMaxDto> getFormItem(Long formItemId) {
        Response<FormItemMaxDto> response=new Response(ResponseCode.SUCCESS,true,"FORM ITEM RETRIEVED SUCCESSFULLY",null);
        try{
        
            Optional<FormItem> existing=formItemRepo.findById(formItemId);
            if(!existing.isPresent()){
            
                response.setMessage("FORM ITEM NOT FOUND");
                return response;
            }
            
            FormItem item=existing.get();
            FormItemMaxDto formItem=new FormItemMaxDto();
            formItem.setId(item.getId());
            formItem.setName(item.getName());
            formItem.setDisplayName(item.getDisplayName());
            formItem.setFlag(item.getFlag());
            formItem.setFeedbackType(item.getFeedbackType());
            if(item.getFeedbackType() == FormFeedbackTypeEnum.FIXED){

                formItem.setOptions(formItemOptionRepo.findByFormItemId(item.getId(), Boolean.TRUE));
            }
            
            response.setData(formItem);
            
        }catch(Exception e){
        
            log.error(e.getLocalizedMessage());
            e.printStackTrace();
        }
        
        return response;
    }

    @Override
    @Transactional
    public Response<FormItem> saveFormItem(FormItemDto data,Long formId) {
        Response<FormItem> response=new Response(ResponseCode.SUCCESS,true,"FORM ITEM SAVED SUCCESSFULLY",null);
        try{
            
            LimsUser actor=utility.getUser();
            
            FormItem formItem=new FormItem();
                
            if(data.getId() > 0){

                Optional<FormItem> existing=formItemRepo.findById(data.getId());

                if(!existing.isPresent()){

                    throw new DataNotFoundException("FORM ITEM NOT FOUND ["+data.getId()+"]");
                }

                formItem=existing.get();
                formItem.setUpdatedAt(LocalDateTime.now());
                formItem.setUpdatedBy(actor);
            }else{

                formItem.setCreatedBy(actor);
            }

            formItem.setFeedbackType(data.getFeedbackType());
            formItem.setName(data.getName());
            formItem.setDisplayName(data.getDisplayName());
            formItem.setParent(data.getParent());
            formItem.setFormId(formId);
            formItem.setFlag(data.getFlag());

            formItem=formItemRepo.saveAndFlush(formItem);
            if(data.getFeedbackType() == FormFeedbackTypeEnum.FIXED){

                if(data.getOptions().size() == 0){

                    throw new DataNotFoundException("FEEDBACK OPTIONS FOR ITEM MISSING");
                }

                //save options
                if(data.getId() > 0){

                    formItemOptionRepo.deactivateFormItemOption(data.getId());
                }

                for(String option : data.getOptions()){

                    FormItemOption opt=new FormItemOption();

                    Optional<FormItemOption> existingItemOption=formItemOptionRepo.findByFormItemIdAndName(formItem.getId(), option);
                    if(existingItemOption.isPresent()){

                        opt=existingItemOption.get();
                        opt.setUpdatedAt(LocalDateTime.now());
                        opt.setActive(Boolean.TRUE);
                    }

                    opt.setFormItemId(formItem.getId());
                    opt.setName(option);

                    formItemOptionRepo.save(opt);
                }                
            }
            
            response.setData(formItem);
            
        }catch(DataNotFoundException e){
        
            log.error(e.getLocalizedMessage());
            throw new DataNotFoundException(e.getLocalizedMessage());
        }catch(Exception e){
        
            log.error(e.getLocalizedMessage());
            e.printStackTrace();
            throw new GeneralException("INTERNAL_SERVER_ERROR");
        }
        return response;
    }
    
}
