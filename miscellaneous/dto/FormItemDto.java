/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.miscellaneous.dto;

import java.util.List;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.Data;
import tz.go.tcra.lims.miscellaneous.enums.FormFeedbackTypeEnum;
import tz.go.tcra.lims.miscellaneous.enums.FormFlagEnum;

/**
 *
 * @author emmanuel.mfikwa
 */
@Data
public class FormItemDto {
    
    @Min(0)
    private Long id;
    
    @NotNull(message="name is required")
    private String name;
    
    @NotNull(message="display name is required")
    private String displayName;
    
    @NotNull(message="flag is required")
    private FormFlagEnum flag;
    
    @Min(0)
    private Long parent;
    
    @NotNull(message="feedback type is required")
    private FormFeedbackTypeEnum feedbackType;
    
    @NotNull(message="options cannot be null")
    private List<String> options;
}
