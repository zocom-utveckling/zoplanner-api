package com.zo.webapi.repository;

import com.zo.webapi.model.ClassGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ClassGroupRepository extends JpaRepository<ClassGroup, Long> {
    List<ClassGroup> findByCustomerId(Long customerId);
    List<ClassGroup> findByName(String name);
}