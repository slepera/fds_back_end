package com.repos;

import com.entities.Log;
import com.entities.LogData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogRepository extends JpaRepository<LogData, Long> {

    @Query(value = "SELECT * FROM log WHERE id >= :lastId order by id desc limit 20", nativeQuery = true)
    List<LogData> findByIdGreaterThan(
            @Param("lastId") Long lastId);
}
