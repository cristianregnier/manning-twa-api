package com.twa.flights.api.clusters.controller;

import com.twa.flights.api.clusters.controller.documentation.ClustersResources;
import com.twa.flights.api.clusters.dto.ClusterSearchDTO;
import com.twa.flights.api.clusters.dto.request.ClustersAvailabilityRequestDTO;
import com.twa.flights.api.clusters.service.ClustersService;
import com.twa.flights.api.clusters.validator.AvailabilityRequestValidator;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class ClustersController implements ClustersResources {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClustersController.class);

    private final ClustersService clustersService;

    private final AvailabilityRequestValidator requestValidator;

    @Autowired
    public ClustersController(ClustersService clustersService, AvailabilityRequestValidator requestValidator) {
        this.clustersService = clustersService;
        this.requestValidator = requestValidator;
    }

    @Override
    @RateLimiter(name = "priceItineraries", fallbackMethod = "fallback")
    public ResponseEntity<ClusterSearchDTO> availability(ClustersAvailabilityRequestDTO request) {
        LOGGER.debug("Obtain all the itineraries with price");
        requestValidator.validate(request);

        ClusterSearchDTO response = clustersService.availability(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @SuppressWarnings("unsued")
    private ResponseEntity<Void> fallback(ClustersAvailabilityRequestDTO request, RequestNotPermitted requestNotPermitted) {
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
    }

}
