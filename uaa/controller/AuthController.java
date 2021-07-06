/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.uaa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tz.go.tcra.lims.uaa.dto.AuthRequestDto;
import tz.go.tcra.lims.uaa.dto.AuthResponseDto;
import tz.go.tcra.lims.uaa.dto.AuthResponseMinDto;
import tz.go.tcra.lims.uaa.service.AuthenticationService;
import tz.go.tcra.lims.utils.Response;

/**
 *
 * @author emmanuel.mfikwa
 */
@RestController
@RequestMapping("/v1")
public class AuthController {
    
    @Autowired
    private AuthenticationService service;
    
    @PostMapping("/authenticate")
    public Response<EntityModel<AuthResponseDto>> generateToken(@RequestBody AuthRequestDto authRequest){
       
        return service.authenticate(authRequest);
    }
    
    @PostMapping("/authenticate2")
    public AuthResponseMinDto generateToken2(@RequestBody AuthRequestDto authRequest){
       
        return service.authenticate2(authRequest);
    }
    
    @GetMapping("/refresh-token")
    public Response<String> refreshToken(){
       
        return service.refreshToken();
    }
}
