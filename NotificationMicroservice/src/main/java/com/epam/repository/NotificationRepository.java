package com.epam.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.epam.model.Email;

public interface NotificationRepository extends MongoRepository<Email,String>{

}
