package com.services;

import com.entities.Log;
import com.repos.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogService {

    private final LogRepository logRepository;

    @Autowired
    public LogService(LogRepository log) {
        this.logRepository = log;
    }

    public Log save(Log logTable) {
        return this.logRepository.save(logTable);
    }

    public Log getById(long id) {
        return this.logRepository.findById(id)
                .orElse(null);
    }

    public List<Log> getAllLog(){
        return this.logRepository.findAll();
    }

    public List<Log> findAllByLastId(Long id){
        return this.logRepository.findByIdGreaterThan(id);
    }
}
