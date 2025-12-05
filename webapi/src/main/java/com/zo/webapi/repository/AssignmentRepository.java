package com.zo.webapi.repository;

import com.zo.webapi.model.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {

    List<Assignment> findByConsultant_Id(Long consultantId);
}
