package com.epam.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.epam.entity.Trainee;
import com.epam.entity.Trainer;
import com.epam.entity.Training;

public interface TrainingRepository extends JpaRepository<Training,Integer>{

	List<Training> findAllByTraineeId(int id);

	void deleteByTraineeId(int id);

	List<Training> findByTraineeAndTrainerNotIn(Trainee trainee, List<Trainer> trainerList);

}
