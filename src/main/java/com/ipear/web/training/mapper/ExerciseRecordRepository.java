package com.ipear.web.training.mapper;

import com.ipear.web.training.entity.ExerciseRecord;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ExerciseRecordRepository extends CrudRepository<ExerciseRecord, Integer> {

    ExerciseRecord getExerciseRecordByUid(String uid);

}
