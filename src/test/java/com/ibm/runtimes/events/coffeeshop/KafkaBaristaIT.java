package com.ibm.runtimes.events.coffeeshop;


import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.junit.jupiter.api.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.sse.SseEventSource;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Integration test for the HelloWorld.java.
 * <p>
 * An integration test verifies the workings of a complete program, a module, or a set of dependant classes.
 */
public class KafkaBaristaIT {

    private final static String COFFEESHOP_UI_HOST = System.getenv("COFFEESHOP_UI_HOST");

    @Test
    public void shouldMakeCoffeeWhenOrderedViaKafka() throws InterruptedException {
        Order order = new Order();
               order.setName("Integrator");
        order.setProduct("Appletini");

        CountDownLatch latch = new CountDownLatch(1);

        Client client = ClientBuilder.newClient().register(JacksonJsonProvider.class);

        SseEventSource eventSource = SseEventSource.target(client.target(COFFEESHOP_UI_HOST + "/services/queue")).build();

        AtomicReference<String> readyBeverageRef = new AtomicReference<>();
        eventSource.register(event -> {
            String beverage = event.readData();
            if (beverage.contains("READY")) {
                readyBeverageRef.set(beverage);
                latch.countDown();
            }
        });

        Response response = client.target(COFFEESHOP_UI_HOST + "/services/messaging")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(order));
        assertThat(response.getStatus(),is(200));

        eventSource.open();

        assertTrue(latch.await(1, TimeUnit.MINUTES), "Timeout waiting for events");
        assertThat(readyBeverageRef.get(),containsString("Appletini"));
    }

}
