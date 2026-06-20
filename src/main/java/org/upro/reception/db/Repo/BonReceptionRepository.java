package org.upro.reception.db.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.upro.reception.db.Entity.BonReception;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BonReceptionRepository extends JpaRepository<BonReception, Integer> {


    List<BonReception> findByDateReception(LocalDate date);

    List<BonReception> findByDateReceptionBetween(LocalDate start, LocalDate end);
}