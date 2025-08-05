package com.tow.mandu.repository;

import com.tow.mandu.model.RiderAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RiderAssignmentRepository extends JpaRepository<RiderAssignment, Long> {


}
