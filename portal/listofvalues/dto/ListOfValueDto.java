/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.portal.listofvalues.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ListOfValueDto {
	private Long id;

	private String code;

	private String name;

	private String description;

	private Long parentId;

	private String type;

}
