package ch.hearc.ig.guideresto.persistence;

import ch.hearc.ig.guideresto.business.CompleteEvaluation;
import ch.hearc.ig.guideresto.business.Grade;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

public class CompleteEvaluationMapper {
    private static final Logger logger = LogManager.getLogger(CompleteEvaluationMapper.class);

    public void create(CompleteEvaluation evaluation) {
        Connection conn = ConnectionUtils.getConnection();

        // Insérer le commentaire
        String sqlCommentaire = "INSERT INTO COMMENTAIRES (date_eval, commentaire, nom_utilisateur, fk_rest) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sqlCommentaire, new String[]{"NUMERO"})) {
            stmt.setDate(1, new java.sql.Date(evaluation.getVisitDate().getTime()));
            stmt.setString(2, evaluation.getComment());
            stmt.setString(3, evaluation.getUsername());
            stmt.setInt(4, evaluation.getRestaurant().getId());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int commentId = generatedKeys.getInt(1);
                        evaluation.setId(commentId);

                        // Insérer les notes
                        insertGrades(conn, evaluation);

                        conn.commit();
                        logger.info("Évaluation complète créée avec l'ID : {}", commentId);
                    }
                }
            }

        } catch (SQLException e) {
            logger.error("Erreur lors de la création de l'évaluation complète : {}", e.getMessage());
            try {
                conn.rollback();
            } catch (SQLException ex) {
                logger.error("Erreur lors du rollback : {}", ex.getMessage());
            }
        }
    }

    private void insertGrades(Connection conn, CompleteEvaluation evaluation) throws SQLException {
        String sqlNote = "INSERT INTO NOTES (note, fk_comm, fk_crit) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sqlNote, new String[]{"NUMERO"})) {
            for (Grade grade : evaluation.getGrades()) {
                stmt.setInt(1, grade.getGrade());
                stmt.setInt(2, evaluation.getId());
                stmt.setInt(3, grade.getCriteria().getId());
                stmt.executeUpdate();

                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        grade.setId(generatedKeys.getInt(1));
                    }
                }
            }
        }
    }
}
