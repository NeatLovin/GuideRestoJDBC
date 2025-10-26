package ch.hearc.ig.guideresto.service;

import ch.hearc.ig.guideresto.business.City;
import ch.hearc.ig.guideresto.persistence.CityMapper;
import ch.hearc.ig.guideresto.persistence.ConnectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Set;

public class CityService {
    private static final Logger logger = LogManager.getLogger();

    private final CityMapper cityMapper = new CityMapper();

    public Set<City> findAll() {
        return cityMapper.findAll();
    }

    public City create(City city) {
        Connection conn = ConnectionUtils.getConnection();
        City created = cityMapper.create(city);
        try { conn.commit(); } catch (SQLException e) { logger.error("Commit failed: {}", e.getMessage()); }
        return created;
    }
}

