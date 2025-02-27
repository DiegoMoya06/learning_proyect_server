package com.dm.learning.controllers;

import com.dm.learning.services.DSService;
import com.dm.learning.utils.BaseLogger;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping(path = "/ds_controller")
//TODO: will be changed when having token
@CrossOrigin(maxAge = 3600)
public class DSController extends BaseLogger {

    private final DSService dsService;

    public DSController(DSService dsService) {
        this.dsService = dsService;
    }

    @PostMapping("/send-input-from-file")
    public String sendInputFromFile(@RequestParam MultipartFile file) throws IOException {
        logger.info("DS-sendInputFromFile method was called");
        File tempFile = File.createTempFile("upload-", file.getOriginalFilename());
        file.transferTo(tempFile);
        return dsService.sendChatRequestWithFile(tempFile);
    }
}
