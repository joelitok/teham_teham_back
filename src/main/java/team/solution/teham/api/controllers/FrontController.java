package team.solution.teham.api.controllers;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.SocketUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import team.solution.teham.api.models.ProcessInfo;
import team.solution.teham.api.repositories.ProcessInfoRepository;
import team.solution.teham.core.TehamChildProcessThread;

@RestController
public class FrontController {
    
    private ProcessInfoRepository processInfoRepository;

    public FrontController(ProcessInfoRepository processInfoRepository) {
        this.processInfoRepository = processInfoRepository;
    }

    @PostMapping("/start")
    public ResponseEntity<ProcessInfo> startNewProcess(@RequestParam("file") MultipartFile file) {
        try {
            
            int port = SocketUtils.findAvailableTcpPort();

            var t = new TehamChildProcessThread(port, file.getInputStream());
            
            t.setUncaughtExceptionHandler((Thread thread, Throwable e) -> {
                thread.getThreadGroup().uncaughtException(t, e);
                t.stopServer();
            });

            t.start();

            var created = processInfoRepository.save(new ProcessInfo(t.getId(), port));
            
            return ResponseEntity.status(HttpStatus.OK).body(created);

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
        }
    }


    @GetMapping
    public ResponseEntity<List<ProcessInfo>> getAllProcess() {
        return ResponseEntity.ok(processInfoRepository.findAll());
    }
}
