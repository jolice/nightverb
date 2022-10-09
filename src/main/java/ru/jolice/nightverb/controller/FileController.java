package ru.jolice.nightverb.controller;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.jolice.nightverb.repository.FileRepository;

import java.io.File;

@RestController
@RequiredArgsConstructor
public class FileController {

    private final FileRepository fileRepository;

    @GetMapping("/file/download/{path}")
    public ResponseEntity<FileSystemResource> download(@PathVariable("path") String fileUid) {
        val optFile = fileRepository.findById(fileUid);
        if (!optFile.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        val file = new File(optFile.get().path());
        long fileLength = file.length();

        HttpHeaders respHeaders = new HttpHeaders();
        respHeaders.setContentType(new MediaType("audio", "mpeg"));
        respHeaders.setContentLength(fileLength);
        respHeaders.setContentDispositionFormData("attachment", file.getName());

        return new ResponseEntity<>(
                new FileSystemResource(file), respHeaders, HttpStatus.OK
        );
    }

}
