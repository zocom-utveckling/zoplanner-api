package com.zo.webapi.repository;

import com.zo.webapi.model.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ManagerRepository extends JpaRepository<Manager, Long> {

    boolean existsByUserId(Long userId);

    Optional<Manager> findByUserId(Long userId);
}


