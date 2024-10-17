package dev.abidino.export.export.entities;

import dev.abidino.export.export.api.TableHeaderSubType;
import dev.abidino.export.export.api.TableHeaderType;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table
@Data
public class TableHeader {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String header;
    @Enumerated(EnumType.STRING)
    private TableHeaderType tableHeaderType;
    @Enumerated(EnumType.STRING)
    private TableHeaderSubType tableHeaderSubType;
}
