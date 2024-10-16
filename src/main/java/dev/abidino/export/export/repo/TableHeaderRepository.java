package dev.abidino.export.export.repo;

import dev.abidino.export.export.api.ExportType;
import dev.abidino.export.export.entities.TableHeader;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TableHeaderRepository extends JpaRepository<TableHeader, Long> {
    Optional<TableHeader> findByType(ExportType type);
}
