package com.zo.webapi.repository;

import com.zo.webapi.model.Consultant;
import com.zo.webapi.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    List<Customer> findByManagerId(Long managerId);
}
