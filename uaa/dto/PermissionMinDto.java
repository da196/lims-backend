/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.uaa.dto;

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
public class PermissionMinDto {

	private Long id;
	private String name;
	private String groupName;
	private String displayName;
}
