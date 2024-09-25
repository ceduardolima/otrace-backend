package com.celc.otrace.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriBuilder;

import com.celc.otrace.domain.User.User;
import com.celc.otrace.repositories.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.websocket.server.PathParam;
import lombok.AllArgsConstructor;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController()
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    final private UserRepository userRepository;

    @GetMapping("/{id}")
    public ResponseEntity<String> getMethodName(@PathVariable Long id, HttpServletRequest request) {
        log.info(id.toString());
        final User user = userRepository.getReferenceById(id);
        final URI uri = URI.create(request.getRequestURI() + "/" + id);
        return ResponseEntity.created(uri).body("teste");
    }
}
