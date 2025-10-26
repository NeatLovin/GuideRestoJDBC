package ch.hearc.ig.guideresto.service;

import ch.hearc.ig.guideresto.business.BasicEvaluation;
import ch.hearc.ig.guideresto.business.CompleteEvaluation;
import ch.hearc.ig.guideresto.business.Restaurant;
import ch.hearc.ig.guideresto.persistence.BasicEvaluationMapper;
import ch.hearc.ig.guideresto.persistence.CompleteEvaluationMapper;
import ch.hearc.ig.guideresto.persistence.ConnectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.Date;

public class EvaluationService {
    private static final Logger logger = LogManager.getLogger();

    private final BasicEvaluationMapper basicEvaluationMapper = new BasicEvaluationMapper();
    private final CompleteEvaluationMapper completeEvaluationMapper = new CompleteEvaluationMapper();

    public BasicEvaluation addBasicEvaluation(Restaurant restaurant, boolean like, String ipAddress) {
        try {
            BasicEvaluation eval = new BasicEvaluation(null, new Date(), restaurant, like, ipAddress);
            BasicEvaluation created = basicEvaluationMapper.create(eval);
            ConnectionUtils.commit();
            return created;
        } catch (SQLException e) {
            logger.error("Failed to create basic evaluation: {}", e.getMessage(), e);
            try {
                ConnectionUtils.rollback();
            } catch (SQLException ex) {
                logger.error("Rollback failed: {}", ex.getMessage(), ex);
            }
            return null;
        }
    }

    public CompleteEvaluation createCompleteEvaluation(CompleteEvaluation evaluation) {
        try {
            CompleteEvaluation created = completeEvaluationMapper.create(evaluation);
            ConnectionUtils.commit();
            return created;
        } catch (SQLException e) {
            logger.error("Failed to create complete evaluation: {}", e.getMessage(), e);
            try {
                ConnectionUtils.rollback();
            } catch (SQLException ex) {
                logger.error("Rollback failed: {}", ex.getMessage(), ex);
            }
            return null;
        }
    }
}
