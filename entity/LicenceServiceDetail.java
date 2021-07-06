package tz.go.tcra.lims.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author DonaldSj
 */

@Entity
@Table(name = "lims_licence_services", schema = "lims")
@NoArgsConstructor
@Setter
@Getter
public class LicenceServiceDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JsonIgnore
    @Column(name = "uuid", unique = true)
    private UUID uuid = UUID.randomUUID();

    @Column(name = "code",unique = true)
    private String code;

    @Column(name = "name",unique = true)
    private String name;

    @Column(name = "active")
    @Where(clause = "active = true")
    private Boolean active = true;

    @JsonIgnore
    @Column(name = "created_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @CreationTimestamp
    private LocalDateTime createdAt=LocalDateTime.now();

    @JsonIgnore
    @Column(name = "updated_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @JsonIgnore
    @Basic(optional = true)
    @Where(clause = "deleted_at is null")
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}
