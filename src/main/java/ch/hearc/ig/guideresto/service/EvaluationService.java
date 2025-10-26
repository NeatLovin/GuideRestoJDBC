package ch.hearc.ig.guideresto.service;

import ch.hearc.ig.guideresto.business.BasicEvaluation;
import ch.hearc.ig.guideresto.business.CompleteEvaluation;
import ch.hearc.ig.guideresto.business.Restaurant;
import ch.hearc.ig.guideresto.persistence.BasicEvaluationMapper;
import ch.hearc.ig.guideresto.persistence.CompleteEvaluationMapper;
import ch.hearc.ig.guideresto.persistence.ConnectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;

public class EvaluationService {
    private static final Logger logger = LogManager.getLogger();

    private final BasicEvaluationMapper basicEvaluationMapper = new BasicEvaluationMapper();
    private final CompleteEvaluationMapper completeEvaluationMapper = new CompleteEvaluationMapper();

    public BasicEvaluation addBasicEvaluation(Restaurant restaurant, boolean like, String ipAddress) {
        BasicEvaluation eval = new BasicEvaluation(null, new java.util.Date(), restaurant, like, ipAddress);
        Connection conn = ConnectionUtils.getConnection();
        try {
            BasicEvaluation created = basicEvaluationMapper.create(eval);
            conn.commit();
            return created;
        } catch (SQLException e) {
            logger.error("Commit failed: {}", e.getMessage());
            try { conn.rollback(); } catch (SQLException ex) { logger.error("Rollback failed: {}", ex.getMessage()); }
            return null;
        }
    }

    public CompleteEvaluation createCompleteEvaluation(CompleteEvaluation evaluation) {
        Connection conn = ConnectionUtils.getConnection();
        try {
            CompleteEvaluation created = completeEvaluationMapper.create(evaluation);
            conn.commit();
            return created;
        } catch (SQLException e) {
            logger.error("Commit failed: {}", e.getMessage());
            try { conn.rollback(); } catch (SQLException ex) { logger.error("Rollback failed: {}", ex.getMessage()); }
            return null;
        }
    }
}
