/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.miscellaneous.dto;

import lombok.Getter;
import tz.go.tcra.lims.miscellaneous.enums.ListOfValueTypeEnum;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class ListOfValueDto {

	private Long id;
	private String code;

	private String name;

	private String description;

	private Long parentId;

	private ListOfValueTypeEnum type;

}
