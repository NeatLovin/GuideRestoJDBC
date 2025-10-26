package ch.hearc.ig.guideresto.service;

import ch.hearc.ig.guideresto.business.EvaluationCriteria;
import ch.hearc.ig.guideresto.persistence.EvaluationCriteriaMapper;

import java.util.Set;

public class EvaluationCriteriaService {
    private final EvaluationCriteriaMapper criteriaMapper = new EvaluationCriteriaMapper();

    public Set<EvaluationCriteria> findAll() {
        return criteriaMapper.findAll();
    }
}

