/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.licencee.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author emmanuel.mfikwa
 */
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class LicenceeNotificationMinDto {
    
    private Long id;
    private String entityName;
    private String message;
    private String categoryName;
    private String categoryCode;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String createdAt;
}
