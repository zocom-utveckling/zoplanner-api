package com.zo.webapi.specification;

import com.zo.webapi.model.Consultant;
import org.springframework.data.jpa.domain.Specification;

//Denna Spec används till att utföra sökningar med namn, stad, konsultchef för att hitta konsulter
public class ConsultantSpecification {
    public static Specification<Consultant> hasName(String name) {
        return (root, query, criteriaBuilder) -> {
            if(name == null || name.isBlank()) return null;

            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("user").get("name")),
                    "%" + name.toLowerCase().trim() + "%");
        };
    }

    public static Specification<Consultant> hasCity(String city) {
        return (root, query, criteriaBuilder) -> {
            if(city == null || city.isBlank()) return null;
            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("user").get("city")),
                    "%" + city.toLowerCase().trim() + "%");
        };
    }

    public static Specification<Consultant> hasManager(Long managerId) {
        return(root, query, criteriaBuilder) -> {
            if(managerId == null) return null;
            return criteriaBuilder.equal(root.get("manager").get("id"), managerId);
        };
    }
}
