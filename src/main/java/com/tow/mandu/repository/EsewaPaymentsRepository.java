package com.tow.mandu.repository;

import com.tow.mandu.model.EsewaPayments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EsewaPaymentsRepository extends JpaRepository<EsewaPayments, Integer> {
}
