package dev.abidino.export.export.repo;

import dev.abidino.export.export.entities.Request;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestRepository extends JpaRepository<Request, Long> {
}
