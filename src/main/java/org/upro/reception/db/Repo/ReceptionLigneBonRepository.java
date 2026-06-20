package org.upro.reception.db.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.upro.reception.db.Entity.ReceptionLigneBon;

import java.util.List;

@Repository
public interface ReceptionLigneBonRepository extends JpaRepository<ReceptionLigneBon, Integer> {
    List<ReceptionLigneBon> findByBrcp_Id(Integer brcpId);
}