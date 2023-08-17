package com.epam.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.epam.entity.TrainingType;

public interface TrainingTypeRepository extends JpaRepository<TrainingType, Integer>{

	Optional<TrainingType> findByTrainingTypeName(String specialization);

}
