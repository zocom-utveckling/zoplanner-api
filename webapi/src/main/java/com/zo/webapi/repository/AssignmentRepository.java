package com.zo.webapi.repository;

import com.zo.webapi.model.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {

    List<Assignment> findByConsultantId(Long consultantId);

    List<Assignment> findByClassId(Long classId);

    List<Assignment> findByCourseName(String courseName);

    List<Assignment> findByDateStartBetween(LocalDate startDate, LocalDate endDate);

    List<Assignment> findByDateEndAfter(LocalDate date);

    List<Assignment> findByDateEndBefore(LocalDate date);

}
