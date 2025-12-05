package com.zo.webapi.repository;

import com.zo.webapi.model.Consultant;
import com.zo.webapi.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    List<Customer> findByManagerId(Long managerId);

    @Query("select c from Customer c left join fetch c.manager where c.id = :id")
    Optional<Customer> findByIdWithManager(@Param("id") Long id);

    @Query("select c from Customer c left join fetch c.manager")
    List<Customer> findAllWithManagers();
}
