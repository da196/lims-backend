package tz.go.tcra.lims.task.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.AllArgsConstructor;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tz.go.tcra.lims.miscellaneous.enums.TrackableTypeEnum;

/**
 * @author DonaldSj
 */
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class TaskMinDto {

    private Long id;
    private String taskName;
    private Long trackableId;
    private TrackableTypeEnum trackableType;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date dueDate;
    
    
}
