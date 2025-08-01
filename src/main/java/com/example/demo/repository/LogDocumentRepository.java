package com.example.demo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.demo.model.LogDocument;

public interface LogDocumentRepository extends MongoRepository<LogDocument, String> {
}