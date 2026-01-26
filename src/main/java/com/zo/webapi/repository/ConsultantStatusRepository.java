package com.zo.webapi.repository;

import com.zo.webapi.model.ConsultantStatus;
import com.zo.webapi.enums.ConsultantStatusType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ConsultantStatusRepository extends JpaRepository<ConsultantStatus, Long> {

    List<ConsultantStatus> findByConsultantId(Long consultantId);

    List<ConsultantStatus> findByStatus(ConsultantStatusType status);

    @Query("SELECT cs FROM ConsultantStatus cs WHERE cs.consultant.id = :consultantId " +
            "AND cs.dateStart <= :date AND cs.dateEnd >= :date")
    List<ConsultantStatus> findByConsultantIdAndDate(@Param("consultantId") Long consultantId,
                                                     @Param("date") LocalDate date);

    @Query("SELECT cs FROM ConsultantStatus cs WHERE cs.dateStart <= :date AND cs.dateEnd >= :date")
    List<ConsultantStatus> findActiveStatusesOnDate(@Param("date") LocalDate date);
}