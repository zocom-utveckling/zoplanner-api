package com.zo.webapi.repository;

import com.zo.webapi.model.ClassGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClassGroupRepository extends JpaRepository<ClassGroup, Long> {
    List<ClassGroup> findByCustomerId(Long customerId);


    Optional<ClassGroup> findByName(String name);

    // check if class name exists for specific customer to prevent duplicates
    boolean existsByNameAndCustomerId(String name, Long customerId);
}