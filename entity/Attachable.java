package tz.go.tcra.lims.entity;

import tz.go.tcra.lims.miscellaneous.enums.AttachableTypeEnum;

public interface Attachable {
    Long getId();
    AttachableTypeEnum getAttachableType();
}
