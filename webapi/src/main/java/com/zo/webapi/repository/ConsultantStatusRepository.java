package com.zo.webapi.repository;

import com.zo.webapi.model.ConsultantStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsultantStatusRepository extends JpaRepository<ConsultantStatus, Long> {
    List<ConsultantStatus> findByConsultantId(Long consultantId);
}
