package com.controllers;

import com.entities.Log;
import com.entities.LogData;
import com.services.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/log")
public class LogController {

    @Autowired
    private LogService logService;


    @PostMapping
    public Log save(@RequestBody LogData logTable) {
        return logService.save(logTable);
    }

    @GetMapping("/{id}")
    public Log getById(@PathVariable long id) {
        return logService.getById(id);
    }


    private ExecutorService log_thread = Executors.newFixedThreadPool(5);
    @GetMapping("/last/{id}")
    public DeferredResult<List<LogData>> getAll(@PathVariable long id) {
        DeferredResult<List<LogData>> output = new DeferredResult<>(-1L);
        log_thread.execute(() -> {
            int loop_count = 0;
            try {
                List<LogData> res;
                while(((res = logService.findAllByLastId(id)).size()==0)&&(loop_count<10))
                {
                    Thread.sleep(1000);
                    loop_count++;
                }
                output.setResult(res);
            } catch (Exception e) {
                System.out.println(e);
                return;
            }
        });
        return output;
    }
}
