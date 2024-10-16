package dev.abidino.export.export.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table
@Data
public class ColumnHeader {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private TableHeader tableHeader;
    private String name;
    private String header;
    private Integer orderNo;
    private String dataType;
    private Boolean isView;
    private String filterType;
}
