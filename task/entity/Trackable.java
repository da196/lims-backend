package tz.go.tcra.lims.task.entity;

import tz.go.tcra.lims.miscellaneous.enums.TrackableTypeEnum;

public interface Trackable {
    Long getId();
    TrackableTypeEnum getTrackableType();
}
