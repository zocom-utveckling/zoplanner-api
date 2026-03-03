package com.zo.webapi.specification;

import com.zo.webapi.model.Course;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;


//Denna spec används för att hitta kurser. Man kan söka på namn, filtrera efter stad, datum och kund.
public class CourseSpecification {
    //Sök kursnamn
    public static Specification<Course> hasName(String name) {
        return (root, query, criteriaBuilder) -> {
            if(name == null || name.isBlank()) return null;

            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("name")),
                    "%" + name.toLowerCase().trim() + "%");
        };
    }

    //Filtrera datum
    public static Specification<Course> hasDate(LocalDate start, LocalDate end) {
        if(start == null || end == null) return null;

        return (root, query, criteriaBuilder) -> criteriaBuilder.and(
                criteriaBuilder.lessThanOrEqualTo(root.get("dateStart"), end),
                criteriaBuilder.greaterThanOrEqualTo(root.get("dateEnd"), start)
        );
    }

    //Filtrera efter kund/YH
    public static Specification<Course> hasCustomer(Long customerId) {
        return (root, query, criteriaBuilder) -> {
            if(customerId == null) return null;
            return criteriaBuilder.equal(root.get("classGroup").get("customer").get("id"), customerId);
        };
    }

    //Filtrera stad
    public static Specification<Course> hasCity(String city) {
        return (root, query, criteriaBuilder) -> {
            if(city == null || city.isBlank()) return null;
            return criteriaBuilder.equal(criteriaBuilder.lower(root.get("classGroup").get("customer").get("city")), city.toLowerCase());
        };
    }
}
