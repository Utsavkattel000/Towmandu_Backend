package com.tow.mandu.repository;

import com.tow.mandu.model.Seeker;
import com.tow.mandu.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeekerRepository extends JpaRepository<Seeker, Long> {

    Seeker findByUser(User user);
}
