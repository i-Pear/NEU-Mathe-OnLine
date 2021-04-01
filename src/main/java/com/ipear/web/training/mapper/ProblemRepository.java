package com.ipear.web.training.mapper;

import com.ipear.web.training.entity.Problem;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProblemRepository extends CrudRepository<Problem, Integer>{

    Problem getProblemById(int id);

    List<Problem> getProblemsByChapter(String chapter);

    int countProblemsByChapter(String chapter);

}
