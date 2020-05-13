package com.ibm.runtimes.events.coffeeshop;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.junit.jupiter.api.Test;

public class BaristaHttpIT {

    private final static String COFFEESHOP_URI = System.getenv("COFFEESHOP_URI");

    @Test
    public void shouldMakeCoffeeWhenOrderedViaHttp() throws InterruptedException {
        Order order = new Order();
        order.setName("Integrator");
        order.setProduct("Appletini");

        Client client = ClientBuilder.newClient().register(JacksonJsonProvider.class);

        Response response = client.target(COFFEESHOP_URI + "/services/http")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(order));
        assertThat(response.getStatus(),is(200));

        assertThat(response.readEntity(Beverage.class).getBeverage(),is("Appletini"));
    }

}