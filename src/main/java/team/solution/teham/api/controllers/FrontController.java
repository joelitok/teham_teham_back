package team.solution.teham.api.controllers;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.util.SocketUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.xml.sax.SAXException;

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
    public ResponseEntity<ProcessInfo> startNewProcess(
        HttpServletRequest request, 
        @RequestParam("xml_file_name") String xmlFileName
    ) {
        try {
            
            int port = SocketUtils.findAvailableTcpPort();

            var file = ResourceUtils.getFile("classpath:xml_files/" + xmlFileName);

            var requestURL = new URL(request.getRequestURL().toString());
            var host = requestURL.getHost();

            var topic = "/";
            var t = new TehamChildProcessThread(host, port, topic, new FileInputStream(file));
        
            // t.test();

            
            var url = String.format("ws://%s:%s", host, port);

            var created = processInfoRepository.save(new ProcessInfo(t.getId(), url, host, port, topic));
            
            t.setUncaughtExceptionHandler((Thread thread, Throwable e) -> {
                thread.getThreadGroup().uncaughtException(t, e);
                t.stopServer();
                processInfoRepository.deleteById(created.getId());
            });

            t.start();
            
            return ResponseEntity.status(HttpStatus.OK).body(created);

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
        } catch (TehamChildProcessThread.FailedToStartException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(null);
        } catch (ParserConfigurationException | SAXException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);
        }
    }


    @GetMapping
    public ResponseEntity<List<ProcessInfo>> getAllProcess() {
        return ResponseEntity.ok(processInfoRepository.findAll());
    }
}
