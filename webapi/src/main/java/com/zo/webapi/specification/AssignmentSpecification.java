package com.zo.webapi.specification;

import com.zo.webapi.model.Assignment;
import org.springframework.data.jpa.domain.Specification;

public class AssignmentSpecification {

    public static Specification<Assignment> isPublished(Boolean published) {
        return (root, query, criteriaBuilder) -> {
            if(published == null) return null;
            return criteriaBuilder.equal(root.get("published"), published);
        };
    }

    public static Specification<Assignment> hasConsultant(Long consultantId) {
        return (root, query, criteriaBuilder) -> {
            if(consultantId == null) return null;
            return criteriaBuilder.equal(root.get("consultant").get("id"), consultantId);
        };
    }
}
