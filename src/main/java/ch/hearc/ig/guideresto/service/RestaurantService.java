package ch.hearc.ig.guideresto.service;

import ch.hearc.ig.guideresto.business.City;
import ch.hearc.ig.guideresto.business.Restaurant;
import ch.hearc.ig.guideresto.persistence.ConnectionUtils;
import ch.hearc.ig.guideresto.persistence.RestaurantMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Set;

public class RestaurantService {
    private static final Logger logger = LogManager.getLogger();

    private final RestaurantMapper restaurantMapper = new RestaurantMapper();

    /**
     * Retourne tous les restaurants (chargés avec leurs dépendances via le mapper)
     */
    public Set<Restaurant> findAll() {
        return restaurantMapper.findAll();
    }

    /**
     * Crée un restaurant et commit la transaction.
     */
    public Restaurant create(Restaurant restaurant) {
        Connection conn = ConnectionUtils.getConnection();
        try {
            Restaurant created = restaurantMapper.create(restaurant);
            conn.commit();
            return created;
        } catch (SQLException e) {
            logger.error("Commit failed: {}", e.getMessage());
            try { conn.rollback(); } catch (SQLException ex) { logger.error("Rollback failed: {}", ex.getMessage()); }
            return null;
        }
    }

    /**
     * Met à jour un restaurant et commit la transaction.
     */
    public boolean update(Restaurant restaurant) {
        Connection conn = ConnectionUtils.getConnection();
        try {
            boolean ok = restaurantMapper.update(restaurant);
            conn.commit();
            return ok;
        } catch (SQLException e) {
            logger.error("Commit failed: {}", e.getMessage());
            try { conn.rollback(); } catch (SQLException ex) { logger.error("Rollback failed: {}", ex.getMessage()); }
            return false;
        }
    }

    /**
     * Mise à jour pratique de l'adresse (rue + ville) et commit.
     */
    public boolean updateAddress(Restaurant restaurant, String newStreet, City newCity) {
        restaurant.getAddress().setStreet(newStreet);
        restaurant.getAddress().setCity(newCity);
        return update(restaurant);
    }

    /**
     * Supprime un restaurant (ainsi que ses données liées, gérées par le mapper) et commit.
     */
    public boolean delete(Restaurant restaurant) {
        Connection conn = ConnectionUtils.getConnection();
        try {
            boolean ok = restaurantMapper.delete(restaurant);
            conn.commit();
            return ok;
        } catch (SQLException e) {
            logger.error("Commit failed: {}", e.getMessage());
            try { conn.rollback(); } catch (SQLException ex) { logger.error("Rollback failed: {}", ex.getMessage()); }
            return false;
        }
    }
}
