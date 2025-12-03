package com.zo.webapi.repository;

import com.zo.webapi.model.Consultant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConsultantRepository extends JpaRepository<Consultant, Long> {

    Optional<Consultant> findByUserId(Long userId);

    List<Consultant> findByManagerId(Long managerId);

    List<Consultant> findByCity(String city);

    @Query("SELECT c FROM Consultant c WHERE c.manager.id = :managerId AND c.city = :city")
    List<Consultant> findByManagerIdAndCity(@Param("managerId") Long managerId,
                                            @Param("city") String city);
}