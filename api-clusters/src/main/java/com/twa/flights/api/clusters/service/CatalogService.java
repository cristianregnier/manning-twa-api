package com.twa.flights.api.clusters.service;

import com.twa.flights.api.clusters.connector.CatalogConnector;
import com.twa.flights.api.clusters.dto.CityDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class CatalogService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CatalogService.class);

    private CatalogConnector catalogConnector;

    @Autowired
    public CatalogService(CatalogConnector catalogConnector) {
        this.catalogConnector = catalogConnector;
    }

    @Cacheable(value = "cities")
    public CityDTO getCity(String code) {
        LOGGER.debug("Obtain the information for code: {}", code);
        return catalogConnector.getCityByCode(code);
    }
}
