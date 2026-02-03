package com.carecheck.repository;

import com.carecheck.entity.Ward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WardRepository extends JpaRepository<Ward, Long> {
    // Пока методов не нужно, наследуем стандартные (save, findById, findAll, delete...).
}