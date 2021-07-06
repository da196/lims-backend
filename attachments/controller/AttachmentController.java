package tz.go.tcra.lims.attachments.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestParam;
import tz.go.tcra.lims.attachments.services.AttachmentService;
import tz.go.tcra.lims.entity.Attachment;
import tz.go.tcra.lims.utils.Response;

@RestController
@RequestMapping(value = "/v1/attachments")
public class AttachmentController {

    @Autowired
    private AttachmentService service;

    @GetMapping(value = "/list")
    @PreAuthorize("hasRole('ROLE_ATTACHMENTS_VIEW_ALL')")
    public Response<CollectionModel<EntityModel<Attachment>>> getListOfAttachments(
            @RequestParam(name = "page",defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "15") int size,
            @RequestParam(name = "sort_name", defaultValue = "id") String sortName,
            @RequestParam(name = "sort_type", defaultValue = "DESC") String sortType) {
        
            return service.listAll(page,size,sortName,sortType);
    }
    
//    @GetMapping(value = "/list-by-attachable-type")
//    @PreAuthorize("hasRole('ROLE_ATTACHMENTS_VIEW_ALL')")
//    public Response<CollectionModel<EntityModel<Attachment>>> getListOfAttachmentsByAttachableType(
//            @RequestParam(name = "attachableType") String attachableType,
//            @RequestParam(name = "page",defaultValue = "0") int page,
//            @RequestParam(name = "size", defaultValue = "15") int size,
//            @RequestParam(name = "sortName", defaultValue = "id") String sortName,
//            @RequestParam(name = "sortType", defaultValue = "DESC") String sortType) {
//        
//            return service.findByAttachableType(attachableType,page,size,sortName,sortType);
//    }
//    
//    @GetMapping(value = "/list-by-attachment-type")
//    @PreAuthorize("hasRole('ROLE_ATTACHMENTS_VIEW_ALL')")
//    public Response<CollectionModel<EntityModel<Attachment>>> getListOfAttachmentsByType(
//            @RequestParam(name = "attachmentType") Long attachmentType,
//            @RequestParam(name = "page",defaultValue = "0") int page,
//            @RequestParam(name = "size", defaultValue = "15") int size,
//            @RequestParam(name = "sortName", defaultValue = "id") String sortName,
//            @RequestParam(name = "sortType", defaultValue = "DESC") String sortType) {
//        
//            return service.findByAttachmentTypeId(attachmentType,page,size,sortName,sortType);
//    }
//    
//    @GetMapping(value = "/{id}")
//    @PreAuthorize("hasRole('ROLE_ATTACHMENTS_VIEW_DETAILS')")
//    public Response<EntityModel<Attachment>> findAttachmentById(@PathVariable Long id) {
//        
//        return service.getAttachment(id);
//    }
//    
//    @GetMapping(value = "/licencee/{id}")
//    public Response<Attachment> findAttachmentByIdPortal(@PathVariable Long id) {
//        
//        return service.getAttachmentPortal(id);
//    }
}
