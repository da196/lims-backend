/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.uaa.service;

import org.springframework.hateoas.EntityModel;
import tz.go.tcra.lims.uaa.dto.AuthRequestDto;
import tz.go.tcra.lims.uaa.dto.AuthResponseDto;
import tz.go.tcra.lims.uaa.dto.AuthResponseMinDto;
import tz.go.tcra.lims.uaa.entity.LimsUser;
import tz.go.tcra.lims.utils.Response;

/**
 *
 * @author emmanuel.mfikwa
 */
public interface AuthenticationService {
    
    public Response<EntityModel<AuthResponseDto>> authenticate(AuthRequestDto data);
    public AuthResponseMinDto authenticate2(AuthRequestDto data);
    public void saveLogin(LimsUser user_id);
    Response<String> refreshToken();
}
