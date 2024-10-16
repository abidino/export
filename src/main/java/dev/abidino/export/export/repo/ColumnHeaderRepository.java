package dev.abidino.export.export.repo;

import dev.abidino.export.export.entities.ColumnHeader;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ColumnHeaderRepository extends JpaRepository<ColumnHeader, Long> {
    List<ColumnHeader> findAllByTableHeader_IdOrderByOrderNo(Long tableHeaderId);
}
