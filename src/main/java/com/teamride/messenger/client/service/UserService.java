package com.teamride.messenger.client.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teamride.messenger.client.dto.UserDTO;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserService {

    private ObjectMapper objectMapper = new ObjectMapper();

    public UserDTO signUp(MultipartHttpServletRequest req) throws Exception {
        // final String body = req.getParameter("body");
        // final MultipartFile file = req.getFile("file");
        // final String dir = req.getServletContext()
        // .getRealPath("AtchFolder");
        String temp = "";
        try {
            final String body = req.getParameter("body");
            final MultipartFile multipartFile = req.getFile("file");
            final String dir = req.getServletContext()
                .getRealPath("AtchFolder");

            final UserDTO dto = objectMapper.readValue(body, UserDTO.class);
            if (multipartFile != null) {
                final String originalFilename = multipartFile.getOriginalFilename();
                final int lastIndex = originalFilename.lastIndexOf(".");
                final String extension = originalFilename.substring(lastIndex);
                final byte[] bytes = multipartFile.getBytes();

                final UUID uuid = UUID.randomUUID();
                final Path path = Paths.get(dir);
                final Path file = Paths.get(dir + "/" + uuid + extension);
                if (!Files.isDirectory(path)) {
                    Files.createDirectories(path);
                }
                if (Files.notExists(file)) {
                    Files.createFile(file);
                    Files.write(file, bytes, StandardOpenOption.WRITE);
                    temp = file.toString();
                }

                dto.setProfileImg(uuid + extension);
                dto.setProfileOriginalImg(originalFilename);
                dto.setProfilePath(file.toString());
            }
            return dto;
        } catch (Exception e) {
            log.error("Insert Error", e.getLocalizedMessage());
            Files.deleteIfExists(Paths.get(temp));
            throw e;
        }

    }
}
