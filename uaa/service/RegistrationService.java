/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.uaa.service;

import java.util.List;
import tz.go.tcra.lims.uaa.dto.PasswordResetRequestDto;
import tz.go.tcra.lims.uaa.dto.RegistrationDto;
import tz.go.tcra.lims.uaa.dto.SignUpRequestDto;
import tz.go.tcra.lims.utils.SaveResponseDto;

/**
 *
 * @author emmanuel.mfikwa
 */
public interface RegistrationService {
    
    SaveResponseDto saveRegistration(SignUpRequestDto data);
    SaveResponseDto activateUser(String data);
    SaveResponseDto forgotPassword(String email);
    SaveResponseDto savePasswordReset(PasswordResetRequestDto data);
    List<RegistrationDto> listRegistrations();
}
