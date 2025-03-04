package com.dm.learning.controllers;

import com.dm.learning.dto.deck.NewAutomaticDeckDto;
import com.dm.learning.exceptions.ApiRequestException;
import com.dm.learning.exceptions.FileProcessingException;
import com.dm.learning.services.DSService;
import com.dm.learning.utils.BaseLogger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<NewAutomaticDeckDto> sendInputFromFile(@RequestParam MultipartFile file) {
        logger.info("DS-sendInputFromFile method was called");

        try {
            File tempFile = File.createTempFile("upload-", file.getOriginalFilename());
            file.transferTo(tempFile);

            return ResponseEntity.ok(dsService.sendChatRequestWithFile(tempFile));
        } catch (IOException e) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("File processing error: " + e.getMessage());
            throw new FileProcessingException("Failed to process the uploaded file", e);
        } catch (ApiRequestException e) {
            throw new ApiRequestException("Failed to send API request", e);
        }
    }
}
