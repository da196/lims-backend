package tz.go.tcra.lims.entity;

import tz.go.tcra.lims.miscellaneous.entity.ListOfValue;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * @author DonaldSj
 */

@Entity
@Table(name = "lims_licence_individual_application_content_resources", schema = "lims")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class IndividualLicenceApplicationContentResource implements Serializable {

    private static final long serialVersionUID = -6199245727668734490L;

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @JsonIgnore
    @Column(name = "individual_id")
    private Long individualId;
    
    @OneToOne
    @JoinColumn(name = "resource_id")
    private ListOfValue resource;
}
