package ch.hearc.ig.guideresto.persistence;

import ch.hearc.ig.guideresto.business.Restaurant;
import ch.hearc.ig.guideresto.business.City;
import ch.hearc.ig.guideresto.business.RestaurantType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

import static ch.hearc.ig.guideresto.persistence.ConnectionUtils.getConnection;

public class RestaurantMapper extends AbstractMapper<Restaurant> {

    private static final String SELECT_BY_ID = "SELECT fk_vill, fk_type, nom, adresse, description, site_web FROM RESTAURANTS WHERE numero = ?";
    private static final String SELECT_ALL = "SELECT numero, fk_vill, fk_type, nom, adresse, description, site_web FROM RESTAURANTS ORDER BY nom";
    private static final String INSERT = "INSERT INTO RESTAURANTS (fk_vill, fk_type, nom, adresse, description, site_web) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String UPDATE = "UPDATE RESTAURANTS SET fk_vill = ?, fk_type = ?, nom = ?, adresse = ?, description = ?, site_web = ? WHERE numero = ?";
    private static final String DELETE = "DELETE FROM RESTAURANTS WHERE numero = ?";

    private static final String EXISTS_QUERY = "SELECT 1 FROM RESTAURANTS WHERE numero = ?";
    private static final String COUNT_QUERY = "SELECT COUNT(*) FROM RESTAURANTS";
    private static final String SEQUENCE_QUERY = "SELECT SEQ_RESTAURANTS.CURRVAL FROM DUAL";

    private CityMapper cityMapper = new CityMapper();
    private RestaurantTypeMapper typeMapper = new RestaurantTypeMapper();

    @Override
    public Restaurant findById(int id) {
        if (cache.containsKey(id)) {
            return cache.get(id);
        }

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_BY_ID)) {

            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Restaurant restaurant = mapResultSetToObject(resultSet, id);
                    cache.put(id, restaurant);
                    return restaurant;
                }
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }

        return null;
    }

    private Restaurant mapResultSetToObject(ResultSet resultSet, int id) throws SQLException {
        int cityId = resultSet.getInt("fk_vill");
        int typeId = resultSet.getInt("fk_type");
        String name = resultSet.getString("nom");
        String address = resultSet.getString("adresse");
        String description = resultSet.getString("description");
        String website = resultSet.getString("site_web");

        // Récupérer les objets associés
        City city = cityMapper.findById(cityId);
        RestaurantType type = typeMapper.findById(typeId);

        return new Restaurant(id, name, address, description, website, city, type);
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
