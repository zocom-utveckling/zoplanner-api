package com.zo.webapi.repository;

import com.zo.webapi.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository layer for <code>Session</code>.
 * @see com.zo.webapi.model.Session
 */
@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {

    List<Session> findSessionsByAssignmentId(Long id);
}
