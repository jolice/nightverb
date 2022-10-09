package ru.jolice.nightverb.controller;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.val;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.jolice.nightverb.repository.FileRepository;

import javax.servlet.http.HttpServletResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequiredArgsConstructor
public class AudioStreamController {

    private final FileRepository fileRepository;

    @RequestMapping(value = "/stream/{token}")
    @SneakyThrows
    public ResponseEntity<byte[]> getSong(@PathVariable("token") String fileUid, HttpServletResponse response) {

        val optFile = fileRepository.findById(fileUid);
        if (!optFile.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Path path = Paths.get(optFile.get().path());
        response.setContentType("audio/mp3");
        response.setContentLength((int) Files.size(path));
        Files.copy(path, response.getOutputStream());
        response.flushBuffer();
        return null;
    }

}
