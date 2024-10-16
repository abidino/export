package dev.abidino.export.export.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table
@Data
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private TableHeader tableHeader;

    private String requestStatus;
    private Long mediaId;
    private Integer dataCount;
    private String filters;
    private Boolean isJob;

    @ManyToMany
    private List<ColumnHeader> columnHeaders;
}
