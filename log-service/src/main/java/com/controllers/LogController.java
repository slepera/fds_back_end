package com.controllers;

import com.entities.Log;
import com.services.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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

    @GetMapping("/last/{id}")
    public List<Log> getAll(@PathVariable long id){
        return logService.findAllByLastId(id);
    }

    @GetMapping("/stream-sse-mvc")
    public SseEmitter streamSseMvc() {
        SseEmitter emitter = new SseEmitter(-1L);
        ExecutorService sseMvcExecutor = Executors.newSingleThreadExecutor();
        sseMvcExecutor.execute(() -> {
            try {
                for (int i = 0; true; i++) {
                    SseEmitter.SseEventBuilder event = SseEmitter.event()
                            .data("SSE MVC - " + LocalTime.now().toString())
                            .id(String.valueOf(i))
                            .name("sse event - mvc");
                    emitter.send(event);
                    Thread.sleep(1000);
                }
            } catch (Exception ex) {
                emitter.completeWithError(ex);
            }
        });
        return emitter;
    }

}
