package com.controllers;

import com.entities.Log;
import com.services.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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


    private ExecutorService bakers = Executors.newFixedThreadPool(5);
    @GetMapping("/last/{id}")
    public DeferredResult<List<Log>> getAll(@PathVariable long id) {
        DeferredResult<List<Log>> output = new DeferredResult<>(5000L);
        output.onTimeout(() -> output.setErrorResult("the bakery is not responding in allowed time"));
        bakers.execute(() -> {
            try {
                List<Log> res;
                while((res = logService.findAllByLastId(id)).size()==0)
                {
                    Thread.sleep(1000);
                }
                output.setResult(res);
            } catch (Exception e) {
                System.out.println("eccezione in thread");
                System.out.println(e);
                return;
            }
        });
        return output;
    }


}
