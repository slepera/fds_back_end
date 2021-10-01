package com.repos;

import com.entities.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogRepository extends JpaRepository<Log, Long> {

    @Query(value = "SELECT * FROM log WHERE id >= :lastId", nativeQuery = true)
    List<Log> findByIdGreaterThan(
            @Param("lastId") Long lastId);
}
