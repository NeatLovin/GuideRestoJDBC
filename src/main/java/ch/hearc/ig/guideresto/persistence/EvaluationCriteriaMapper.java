package ch.hearc.ig.guideresto.persistence;

import ch.hearc.ig.guideresto.business.EvaluationCriteria;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.Set;

public class EvaluationCriteriaMapper extends AbstractMapper<EvaluationCriteria> {

    private static final String SELECT_BY_ID = "SELECT numero, nom, description FROM CRITERES_EVALUATION WHERE numero = ?";
    private static final String SELECT_ALL = "SELECT numero, nom, description FROM CRITERES_EVALUATION ORDER BY nom";
    private static final String INSERT = "INSERT INTO CRITERES_EVALUATION (nom, description) VALUES (?, ?)";
    private static final String UPDATE = "UPDATE CRITERES_EVALUATION SET nom = ?, description = ? WHERE numero = ?";
    private static final String DELETE = "DELETE FROM CRITERES_EVALUATION WHERE numero = ?";

    private static final String EXISTS_QUERY = "SELECT 1 FROM CRITERES_EVALUATION WHERE numero = ?";
    private static final String COUNT_QUERY = "SELECT COUNT(*) FROM CRITERES_EVALUATION";
    private static final String SEQUENCE_QUERY = "SELECT SEQ_CRITERES_EVALUATION.CURRVAL FROM DUAL";

    @Override
    protected String getSequenceQuery() {
        return SEQUENCE_QUERY;
    }

    @Override
    protected String getExistsQuery() {
        return EXISTS_QUERY;
    }

    @Override
    protected String getCountQuery() {
        return COUNT_QUERY;
    }

    @Override
    public EvaluationCriteria findById(int id) {
        if (cache.containsKey(id)) {
            return cache.get(id);
        }

        Connection connection = ConnectionUtils.getConnection();
        try (PreparedStatement stmt = connection.prepareStatement(SELECT_BY_ID)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    EvaluationCriteria ec = mapRow(rs);
                    addToCache(ec);
                    return ec;
                }
            }
        } catch (SQLException ex) {
            logger.error("SQLException: {}", ex.getMessage());
        }
        return null;
    }

    @Override
    public Set<EvaluationCriteria> findAll() {
        if (!isCacheEmpty()) {
            return new LinkedHashSet<>(cache.values());
        }

        Set<EvaluationCriteria> result = new LinkedHashSet<>();
        Connection connection = ConnectionUtils.getConnection();
        try (PreparedStatement stmt = connection.prepareStatement(SELECT_ALL);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                EvaluationCriteria ec = mapRow(rs);
                result.add(ec);
                addToCache(ec);
            }
        } catch (SQLException ex) {
            logger.error("SQLException: {}", ex.getMessage());
        }
        return result;
    }

    @Override
    public EvaluationCriteria create(EvaluationCriteria object) {
        if (object == null) {
            return null;
        }

        Connection connection = ConnectionUtils.getConnection();
        try (PreparedStatement stmt = connection.prepareStatement(INSERT)) {
            stmt.setString(1, object.getName());
            stmt.setString(2, object.getDescription());

            int affected = stmt.executeUpdate();
            Integer id = getSequenceValue();
            if (id != null && id > 0) {
                object.setId(id);
                addToCache(object);
            }
            try {
                connection.commit();
            } catch (SQLException ex) {
                logger.error("Commit failed: {}", ex.getMessage());
            }
            return object;
        } catch (SQLException ex) {
            logger.error("SQLException: {}", ex.getMessage());
            try {
                connection.rollback();
            } catch (SQLException e) {
                logger.error("Rollback failed: {}", e.getMessage());
            }
        }
        return null;
    }

    @Override
    public boolean update(EvaluationCriteria object) {
        if (object == null || object.getId() == null) {
            return false;
        }

        Connection connection = ConnectionUtils.getConnection();
        try (PreparedStatement stmt = connection.prepareStatement(UPDATE)) {
            stmt.setString(1, object.getName());
            stmt.setString(2, object.getDescription());
            stmt.setInt(3, object.getId());

            int affected = stmt.executeUpdate();
            try {
                connection.commit();
            } catch (SQLException ex) {
                logger.error("Commit failed: {}", ex.getMessage());
            }

            if (affected > 0) {
                addToCache(object);
                return true;
            }
        } catch (SQLException ex) {
            logger.error("SQLException: {}", ex.getMessage());
            try {
                connection.rollback();
            } catch (SQLException e) {
                logger.error("Rollback failed: {}", e.getMessage());
            }
        }
        return false;
    }

    @Override
    public boolean delete(EvaluationCriteria object) {
        if (object == null || object.getId() == null) {
            return false;
        }
        return deleteById(object.getId());
    }

    @Override
    public boolean deleteById(int id) {
        Connection connection = ConnectionUtils.getConnection();
        try (PreparedStatement stmt = connection.prepareStatement(DELETE)) {
            stmt.setInt(1, id);

            int affected = stmt.executeUpdate();
            try {
                connection.commit();
            } catch (SQLException ex) {
                logger.error("Commit failed: {}", ex.getMessage());
            }

            if (affected > 0) {
                removeFromCache(id);
                return true;
            }
        } catch (SQLException ex) {
            logger.error("SQLException: {}", ex.getMessage());
            try {
                connection.rollback();
            } catch (SQLException e) {
                logger.error("Rollback failed: {}", e.getMessage());
            }
        }
        return false;
    }

    /**
     * Recherche des critères dont le nom contient la chaîne passée (insensible à la casse)
     */
    public Set<EvaluationCriteria> findByName(String namePart) {
        Set<EvaluationCriteria> result = new LinkedHashSet<>();
        if (namePart == null) {
            return result;
        }

        Connection connection = ConnectionUtils.getConnection();
        String sql = "SELECT numero, nom, description FROM CRITERES_EVALUATION WHERE UPPER(nom) LIKE ? ORDER BY nom";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "%" + namePart.toUpperCase() + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    EvaluationCriteria ec = mapRow(rs);
                    result.add(ec);
                    addToCache(ec);
                }
            }
        } catch (SQLException ex) {
            logger.error("SQLException: {}", ex.getMessage());
        }
        return result;
    }

    private EvaluationCriteria mapRow(ResultSet rs) throws SQLException {
        Integer id = rs.getInt("numero");
        String name = rs.getString("nom");
        String description = rs.getString("description");

        EvaluationCriteria ec = new EvaluationCriteria(id, name, description);
        return ec;
    }
}

