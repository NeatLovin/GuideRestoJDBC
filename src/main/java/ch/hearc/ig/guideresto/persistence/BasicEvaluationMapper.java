package ch.hearc.ig.guideresto.persistence;

import ch.hearc.ig.guideresto.business.BasicEvaluation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

public class BasicEvaluationMapper {
    private static final Logger logger = LogManager.getLogger(BasicEvaluationMapper.class);

    public void create(BasicEvaluation evaluation) {
        String sql = "INSERT INTO LIKES (appreciation, date_eval, adresse_ip, fk_rest) VALUES (?, ?, ?, ?)";

        try (Connection connection = ConnectionUtils.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"NUMERO"})) {

            stmt.setString(1, evaluation.getLikeRestaurant() ? "T" : "F");
            stmt.setDate(2, new java.sql.Date(evaluation.getVisitDate().getTime()));
            stmt.setString(3, evaluation.getIpAddress());
            stmt.setInt(4, evaluation.getRestaurant().getId());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        evaluation.setId(generatedKeys.getInt(1));
                        logger.info("Like/Dislike créé avec l'ID : {}", evaluation.getId());
                    }
                }
            }

        } catch (SQLException e) {
            logger.error("Erreur lors de la création du Like/Dislike : {}", e.getMessage());
        }
    }
}
