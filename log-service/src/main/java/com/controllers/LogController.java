package com.controllers;

import com.entities.Log;
import com.services.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/log")
public class LogController {

    @Autowired
    private LogService logService;


    @PostMapping
    public Log save(@RequestBody Log logTable) {
        return logService.save(logTable);
    }

    @GetMapping("/{id}")
    public Log getById(@PathVariable long id) {
        return logService.getById(id);
    }

}
