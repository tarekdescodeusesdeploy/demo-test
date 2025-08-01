package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.LogDocument;
import com.example.demo.repository.LogDocumentRepository;
import com.example.demo.repository.UserRepository;

@Service
public class LogDocumentService {
    
    @Autowired
    private LogDocumentRepository repo;

    public void addLog(LogDocument doc) {
        repo.save(doc);
    }

}
