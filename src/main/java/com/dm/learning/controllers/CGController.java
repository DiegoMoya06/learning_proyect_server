package com.dm.learning.controllers;

import com.dm.learning.utils.BaseLogger;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/cg_controller")
//TODO: will be changed when having token
@CrossOrigin(maxAge = 3600)
public class CGController extends BaseLogger {

//    private final CGService cgService;

//    public CGController(CGService cgService) {
//        this.cgService = cgService;
//    }

//    @PostMapping("/send-input-from-file")
//    public String sendInputFromFile(@RequestParam MultipartFile file) throws IOException {
//        logger.info("CG-sendInputFromFile method was called");
//        File tempFile = File.createTempFile("upload-", file.getOriginalFilename());
//        file.transferTo(tempFile);
//        return cgService.sendChatRequestWithFile(tempFile);
//    }
//
//    @PostMapping("/send-input")
//    public String sendInput(@RequestParam String message) {
//        return cgService.sendChatRequest(message);
//    }
//
//    @PostMapping("/upload-file")
//    public String uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
//        File tempFile = File.createTempFile("upload-", file.getOriginalFilename());
//        file.transferTo(tempFile);
//        return cgService.uploadFile(tempFile);
//    }
}
