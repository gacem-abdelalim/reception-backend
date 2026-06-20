package org.upro.reception.db.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.upro.reception.db.Entity.DynamicQuery;

import java.util.Optional;

@Repository
public interface DynamicQueryRepository extends JpaRepository<DynamicQuery, Long> {
    Optional<DynamicQuery> findByName(String name);
}

