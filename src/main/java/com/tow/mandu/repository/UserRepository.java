package com.tow.mandu.repository;

import com.tow.mandu.model.User;
import com.tow.mandu.projection.AdminDashboardProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "SELECT * FROM user WHERE email = :email", nativeQuery = true)
    User getByEmail(String email);

    @Query(value = "SELECT EXISTS (SELECT 1 FROM user WHERE email = :email)", nativeQuery = true)
    Integer existsByEmailRaw(@Param("email") String email);

    default boolean existsByEmail(String email) {
        return existsByEmailRaw(email) == 1;
    }

    @Query(value = "SELECT " +
            " (SELECT COUNT(*) FROM user WHERE role <> 'ADMIN') AS totalUsers, " +
            " (SELECT COUNT(*) FROM provider) AS totalProviders, " +
            " (SELECT COUNT(*) FROM provider WHERE approve_status = false) AS totalPendingProviders, " +
            " (SELECT COUNT(*) FROM provider WHERE is_active = true) AS activeProviders",
            nativeQuery = true)
    AdminDashboardProjection getAdminDashboardData();
}