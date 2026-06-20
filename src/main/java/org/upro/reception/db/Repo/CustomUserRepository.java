package org.upro.reception.db.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.upro.reception.db.Entity.CustomUser;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomUserRepository extends JpaRepository<CustomUser, Integer> {
    Optional<CustomUser> findByUsername(String username);


 }