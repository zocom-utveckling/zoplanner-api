package com.zo.webapi.service;

import com.zo.webapi.model.Assignment;
import com.zo.webapi.model.Session;
import com.zo.webapi.repository.SessionRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SessionService {

    private final SessionRepository sessionRepository;
    private final AssignmentService assignmentService;

    public SessionService(SessionRepository sessionRepository, AssignmentService assignmentService) {
        this.sessionRepository = sessionRepository;
        this.assignmentService = assignmentService;
    }

    public List<Session> getAllSessions() {
        return  sessionRepository.findAll();
    }

    public Optional<Session> getSessionById(Long id) {
        return sessionRepository.findById(id);
    }

    public List<Session> getSessionsByAssignmentId(Long id) {
        return sessionRepository.findSessionsByAssignmentId(id);
    }

    @Transactional
    public Session createSession(Long id, Session session) {
        Optional<Assignment> assignment = assignmentService.getAssignmentById(id);
        if(assignment.isPresent()){
            session.setAssignment(assignment.get());
            session.setId(null);
            return sessionRepository.save(session);
        } else return null;
    }

    @Transactional
    public Session updateSession(Long id, Session session) {
        Session updatedSession = sessionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Session with id " + id + " does not exist."));
        //Update the fields with the new data
        updatedSession.setTimeStart(session.getTimeStart());
        updatedSession.setTimeEnd(session.getTimeEnd());
        return sessionRepository.save(updatedSession);
    }

    @Transactional
    public boolean deleteSessionById(Long id) {
        if (!sessionRepository.existsById(id)) {
            return false;
        }

        sessionRepository.deleteById(id);
        return true;
    }
}
