package com.zo.webapi.service;

import com.zo.webapi.model.ClassGroup;
import com.zo.webapi.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClassGroupService {

    private ClassGroupRepository classGroupRepo;

    @Autowired
    public ClassGroupService(ClassGroupRepository classGroupRepo) {
       this.classGroupRepo = classGroupRepo;
    }

    public List<ClassGroup> getAllClass() {
        return classGroupRepo.findAll();
    }

    public Optional<ClassGroup> getClassById(Long id) {
        return classGroupRepo.findById(id);
    }

    public List<ClassGroup> getClassByCustomer(Long customerId) {
        return classGroupRepo.findByCustomerId(customerId);
    }

    @Transactional
       public ClassGroup createClass(ClassGroup newClass) {
        return classGroupRepo.save(newClass);
    }

    @Transactional
    public ClassGroup updateClass(Long id, ClassGroup updatedClass) {
        ClassGroup existing = classGroupRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Class not found with id: " + id));

        existing.setName(updatedClass.getName());
        existing.setCustomerId(updatedClass.getCustomerId());

        return classGroupRepo.save(existing);
    }

    @Transactional
    public void deleteClass(Long id) {
        if (!classGroupRepo.existsById(id)) {
            throw new RuntimeException("Class not found with id: " + id);
        }
        classGroupRepo.deleteById(id);
    }

}
