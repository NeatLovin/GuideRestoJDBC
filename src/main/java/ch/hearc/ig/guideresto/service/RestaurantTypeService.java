package ch.hearc.ig.guideresto.service;

import ch.hearc.ig.guideresto.business.RestaurantType;
import ch.hearc.ig.guideresto.persistence.RestaurantTypeMapper;

import java.util.Set;

public class RestaurantTypeService {
    private final RestaurantTypeMapper typeMapper = new RestaurantTypeMapper();

    public Set<RestaurantType> findAll() {
        return typeMapper.findAll();
    }
}

