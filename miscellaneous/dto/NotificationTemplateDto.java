/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.miscellaneous.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author emmanuel.mfikwa
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationTemplateDto {
    
    private String name;
    private String displayName;
    private String textTemplate;
    private String emailTemplate;
    private String staffEmailTemplate;
}
