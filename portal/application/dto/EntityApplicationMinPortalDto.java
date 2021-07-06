package tz.go.tcra.lims.portal.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author DonaldSj
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EntityApplicationMinPortalDto {

    private Long id;
    private String entityName;
    private Boolean isDraft;
    private String status;
    private Long productId;
    private String displayName;
    private String comment;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime approvedAt;
}
