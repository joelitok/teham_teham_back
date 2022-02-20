package team.solution.teham.api.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class FrontController {
    
    @PostMapping("/start")
    public ResponseEntity<Integer> startNewProcess(@RequestParam("file") MultipartFile file) {
        
        return ResponseEntity.status(HttpStatus.OK).body(0);
    }

}
