package tz.go.tcra.lims.uaa.dto;

import lombok.Getter;
import lombok.Setter;
import tz.go.tcra.lims.uaa.entity.Permission;

import java.util.List;

@Getter
@Setter
public class PermissionGroupDto {
    private String groupName;
    private List<Permission> permissions;

    public PermissionGroupDto(String groupName, List<Permission> permissions) {
        this.groupName = groupName;
        this.permissions = permissions;
    }
}