/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.task.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tz.go.tcra.lims.uaa.dto.RoleMinDto;

/**
 *
 * @author emmanuel.mfikwa
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TrackDto {
    
    private Long id;
    private RoleMinDto actorRole;
    private String actor;
    private Boolean isActed;
    private String stepName;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date dueDate;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime completedAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
}
