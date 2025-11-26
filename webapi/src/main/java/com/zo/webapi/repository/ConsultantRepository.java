package com.zo.webapi.repository;

import com.zo.webapi.model.Consultant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConsultantRepository extends JpaRepository<Consultant, Long> {

    Optional<Consultant> findByUserId(Long userId);

    List<Consultant> findByManagerId(Long managerId);

    List<Consultant> findByCity(String city);

    boolean existsByUserId(Long userId);
}