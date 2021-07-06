/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.uaa.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author emmanuel.mfikwa
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCreationRequestDto {
    
    @NotNull(message = "first name is required")
    private String firstName;
    
    @NotNull(message = "middle name is required")
    private String middleName;
    
    @NotNull(message = "last name is required")
    private String lastName;
    
    @NotNull(message = "adAuthentication is required")
    private boolean adAuthentication;
    
    
    @NotNull(message="phone number is invalid")
//    @Pattern(regexp="^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$",message="phone number is invalid")
    private String phone;
    
    @Email(message="email address is not valid")
    @NotNull
    private String email;
}
