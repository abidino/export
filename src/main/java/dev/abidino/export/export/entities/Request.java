package dev.abidino.export.export.entities;

import dev.abidino.export.export.api.RequestStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
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

    @Enumerated(EnumType.STRING)
    private RequestStatus requestStatus;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<Long> mediaIds = new ArrayList<>();
    private Integer dataCount;
    private String filters;
    private Boolean isJob;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<ColumnHeader> columnHeaders = new ArrayList<>();

    public void addColumnHeader(ColumnHeader columnHeader) {
        this.columnHeaders.add(columnHeader);
    }

    public void addColumnHeader(List<ColumnHeader> columnHeaders) {
        this.columnHeaders.addAll(columnHeaders);
    }


    public void addMediaId(Long mediaId) {
        this.mediaIds.add(mediaId);
    }

    public void addMediaId(List<Long> mediaIds) {
        this.mediaIds.addAll(mediaIds);
    }

}
