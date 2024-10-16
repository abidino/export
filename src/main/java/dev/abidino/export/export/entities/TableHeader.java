package dev.abidino.export.export.entities;

import dev.abidino.export.export.api.ExportType;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table
@Data
public class TableHeader {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private ExportType type;
    private String header;
}
