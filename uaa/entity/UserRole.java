/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.uaa.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

/**
 *
 * @author emmanuel.mfikwa
 */
@Entity
@Table(name="lims_user_roles", schema="lims")
@NoArgsConstructor
@Setter
@Getter
public class UserRole implements Serializable{
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;
    
    @Column(name="user_id")
    private Long userID;
    
    @Column(name="role_id")
    private Long roleID;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "date_created")
    @CreationTimestamp
    private LocalDateTime createdAt=LocalDateTime.now();
    
    @Column(name="unique_id")
    private UUID uniqueID=UUID.randomUUID();

    public UserRole(Long userID, Long role) {
        this.userID = userID;
        this.roleID = role;
    }
}
