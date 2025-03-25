package com.banreservas.product.config;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;

@RegisterRestClient(configKey="exchange-rate-api")
public interface ExchangeRateClient {

    @GET
    @Path("/v6/{apikey}/pair/{from}/{to}")
    ExchangeRate getExchangeRate(@PathParam("apikey") String apiKey,
                                 @PathParam("from") String fromCurrency,
                                 @PathParam("to") String toCurrency);
}
