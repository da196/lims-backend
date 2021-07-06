/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.attachments.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author emmanuel.mfikwa
 */
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class AttachmentMaxDto {
    
    private String uri;
    private String name;
    private Long attachmentTypeId;
    private String attachmentTypeName;
}
