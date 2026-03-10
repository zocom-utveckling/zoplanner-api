package com.zo.webapi.repository;


import com.zo.webapi.model.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

import java.time.LocalDate;

@Repository
public interface ActivityRepository extends JpaRepository <Activity, Long> {
    // Hämta alla aktiviteter mellan två datum, sorterat först -> sist
    List<Activity> findByDateBetweenOrderByDateAscStartTimeAsc(LocalDate from, LocalDate to);
    // Hämta alla aktiviteter, sorterat från först -> sist
    List<Activity> findAllByOrderByDateAscStartTimeAsc();

    // Samma som ovan men baserat på consultantId
    List<Activity> findByConsultantIdOrderByDateAscStartTimeAsc(Long consultantId);

    List<Activity> findByConsultantIdAndDateBetweenOrderByDateAscStartTimeAsc(
            Long consultantId, LocalDate from, LocalDate to
    );
}
