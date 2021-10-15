package com.services;

import com.entities.LogData;
import com.repos.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LogService {

    private final LogRepository logRepository;

    @Autowired
    public LogService(LogRepository log) {
        this.logRepository = log;
    }

    public LogData save(LogData logTable) {
        return this.logRepository.save(logTable);
    }

    public LogData getById(long id) {
        return this.logRepository.findById(id)
                .orElse(null);
    }

    public List<LogData> getAllLog(){
        return this.logRepository.findAll();
    }

    public List<LogData> findAllByLastId(Long id){
        List<LogData> arrayList = this.logRepository.findByIdGreaterThan(id);
        return this.logRepository.findByIdGreaterThan(id);
    }
}
