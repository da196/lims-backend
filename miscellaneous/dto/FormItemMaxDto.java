/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.miscellaneous.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tz.go.tcra.lims.miscellaneous.enums.FormFeedbackTypeEnum;
import tz.go.tcra.lims.miscellaneous.enums.FormFlagEnum;

/**
 *
 * @author emmanuel.mfikwa
 */
@NoArgsConstructor
@Setter
@Getter
public class FormItemMaxDto {
    
    private Long id;
    private Long formId;
    private String name;
    private String displayName;
    private FormFlagEnum flag;
    private FormFeedbackTypeEnum feedbackType;
    private List<FormItemOptionMinDto> options=new ArrayList();
}
