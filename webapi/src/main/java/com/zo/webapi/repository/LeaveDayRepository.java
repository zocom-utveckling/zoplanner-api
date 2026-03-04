package com.zo.webapi.repository;

import com.zo.webapi.model.LeaveDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeaveDayRepository extends JpaRepository<LeaveDay, Long> {

    List<LeaveDay> findByConsultantId(Long consultantId);
}
