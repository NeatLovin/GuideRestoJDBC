package ch.hearc.ig.guideresto.persistence;

import ch.hearc.ig.guideresto.business.Restaurant;

import java.util.Set;

public class RestaurantTypeMapper extends AbstractMapper<Restaurant> {
    private static final String SELECT_BY_ID = "SELECT numero, libelle, description FROM TYPES_GASTRONOMIQUES WHERE numero = ?";
    private static final String SELECT_ALL = "SELECT numero, libelle, description FROM TYPES_GASTRONOMIQUES ORDER BY libelle";
    private static final String INSERT = "INSERT INTO TYPES_GASTRONOMIQUES (libelle, description) VALUES (?, ?)";
    private static final String UPDATE = "UPDATE TYPES_GASTRONOMIQUES SET libelle = ?, description = ? WHERE numero = ?";
    private static final String DELETE = "DELETE FROM TYPES_GASTRONOMIQUES WHERE numero = ?";

    private static final String EXISTS_QUERY = "SELECT 1 FROM TYPES_GASTRONOMIQUES WHERE numero = ?";
    private static final String COUNT_QUERY = "SELECT COUNT(*) FROM TYPES_GASTRONOMIQUES";
    private static final String SEQUENCE_QUERY = "SELECT SEQ_TYPES_GASTRONOMIQUES.CURRVAL FROM DUAL";

    @Override
    public Restaurant findById(int id) {
        return null;
    }

    @Override
    public Set<Restaurant> findAll() {
        return Set.of();
    }

    @Override
    public Restaurant create(Restaurant object) {
        return null;
    }

    @Override
    public boolean update(Restaurant object) {
        return false;
    }

    @Override
    public boolean delete(Restaurant object) {
        return false;
    }

    @Override
    public boolean deleteById(int id) {
        return false;
    }

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
}
