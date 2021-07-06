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
import tz.go.tcra.lims.miscellaneous.enums.FormTypeEnum;

/**
 *
 * @author emmanuel.mfikwa
 */
@Getter
@Setter
@NoArgsConstructor
public class FormMaxDto {
    
    private Long id;
    private String code;
    private String name;
    private String description;
    private FormTypeEnum formType;
    private List<FormItemMinDto> items=new ArrayList();
}
