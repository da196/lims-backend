/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.uaa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tz.go.tcra.lims.uaa.dto.PasswordResetRequestDto;
import tz.go.tcra.lims.utils.SaveResponseDto;
import tz.go.tcra.lims.uaa.dto.SignUpRequestDto;
import tz.go.tcra.lims.uaa.service.RegistrationService;

/**
 *
 * @author emmanuel.mfikwa
 */
@RestController
@RequestMapping("/v1/user-registration")
public class RegistrationController {
    
    @Autowired
    private RegistrationService service;
    
    @PostMapping("/signup")
    public SaveResponseDto saveUserSignup(@RequestBody SignUpRequestDto data) throws Exception {
       
        return service.saveRegistration(data);
    }
    
    @GetMapping("/activation")
    public SaveResponseDto activateAccount(@RequestParam("token") String data){
       
        return service.activateUser(data);
    }
    
    @PostMapping("/forgot-password")
    public SaveResponseDto forgotPassword(@RequestParam("email") String data){
       
        return service.forgotPassword(data);
    }
    
    @PutMapping("/reset-password")
    public SaveResponseDto resetPassword(@RequestBody PasswordResetRequestDto data){
       
        return service.savePasswordReset(data);
    }
}
