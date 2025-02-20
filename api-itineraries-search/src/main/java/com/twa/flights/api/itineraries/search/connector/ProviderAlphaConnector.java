package com.twa.flights.api.itineraries.search.connector;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.twa.flights.api.itineraries.search.connector.filter.ConnectorFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.twa.flights.api.itineraries.search.connector.configuration.ProviderAlphaConnectorConfiguration;
import com.twa.flights.api.itineraries.search.exception.TWAException;
import com.twa.flights.common.dto.itinerary.ItineraryDTO;
import com.twa.flights.common.dto.request.AvailabilityRequestDTO;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import reactor.netty.http.client.HttpClient;

@Component
public class ProviderAlphaConnector {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProviderAlphaConnector.class);

    private static final String SEARCH = "/api/flights/provider/alpha/itineraries";

    public static final String GZIP = "gzip";

    private final ProviderAlphaConnectorConfiguration configuration;

    @Autowired
    public ProviderAlphaConnector(ProviderAlphaConnectorConfiguration configuration) {
        this.configuration = configuration;
    }

    public List<ItineraryDTO> availability(AvailabilityRequestDTO request) {
        final long readTimeout = configuration.getReadTimeout();

        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, Math.toIntExact(configuration.getConnectionTimeout()))
                .responseTimeout(Duration.ofMillis(configuration.getResponseTimeout()))
                .doOnConnected(conn -> conn.addHandlerLast(new ReadTimeoutHandler(readTimeout, TimeUnit.MILLISECONDS)))
                .compress(true);

        ClientHttpConnector connector = new ReactorClientHttpConnector(httpClient);

        WebClient client = WebClient.builder().defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE,
                HttpHeaders.ACCEPT_ENCODING, GZIP)
                .filter(ConnectorFilter.logRequest()).filter(ConnectorFilter.logResponse()).clientConnector(connector)
                .clientConnector(connector).build();

        return client.get()
                .uri(uriBuilder -> uriBuilder.path(configuration.getHost().concat(SEARCH))
                        .queryParam("adults", request.getAdults()).queryParam("children", request.getChildren())
                        .queryParam("infants", request.getInfants()).queryParam("amount", request.getAmount())
                        .queryParam("departure", request.getDeparture()).queryParam("from", request.getFrom())
                        .queryParam("to", request.getTo()).build())
                .retrieve().onStatus(HttpStatus::isError, clientResponse -> {
                    LOGGER.error("Error while calling endpoint {} with status code {}", SEARCH,
                            clientResponse.statusCode());
                    throw new TWAException("Error while calling catalog endpoint");
                }).toEntityList(ItineraryDTO.class).block().getBody();
    }
}
