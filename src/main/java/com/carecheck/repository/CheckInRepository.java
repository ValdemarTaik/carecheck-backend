package com.carecheck.repository;

import com.carecheck.entity.CheckIn;
import com.carecheck.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CheckInRepository extends JpaRepository<CheckIn, Long> {
    List<CheckIn> findByWardOrderByCheckInTimeDesc(User ward);
    Optional<CheckIn> findFirstByWardOrderByCheckInTimeDesc(User ward);
}