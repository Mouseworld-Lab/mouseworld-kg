package com.candil.camel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.support.DefaultExchange;
import org.junit.jupiter.api.Test;

public class AddHeaderAggregationStrategyTest {

    @Test
    public void testAggregateWithBodyExchange() {
        // Arrange
        AddHeaderAggregationStrategy strategy = new AddHeaderAggregationStrategy("IsBody", "node-id");
        Exchange bodyExchange = new DefaultExchange(new DefaultCamelContext());
        bodyExchange.getIn().setBody("{\"name\": \"John Doe\", \"age\": 30}");
        bodyExchange.getIn().setHeader("IsBody", true);

        Exchange headerExchange = new DefaultExchange(new DefaultCamelContext());
        headerExchange.getIn().setHeader("node-id", "router1");
        headerExchange.getIn().setHeader("IsBody", false);

        // Act
        Exchange result = strategy.aggregate(bodyExchange, headerExchange);

        // Assert
        assertNotNull(result);
        assertEquals("{\"name\": \"John Doe\", \"age\": 30}", result.getIn().getBody(String.class));
        assertEquals("router1", result.getIn().getHeader("node-id"));

        System.out.println("Body is " + result.getIn().getBody(String.class) + "\n and header: " + result.getIn().getHeader("node-id"));


    }

    @Test
    public void testAggregateWithHeaderExchangeFirst() {
        // Arrange
        AddHeaderAggregationStrategy strategy = new AddHeaderAggregationStrategy("IsBody", "node-id");
        Exchange headerExchange = new DefaultExchange(new DefaultCamelContext());
        headerExchange.getIn().setHeader("node-id", "router1");
        headerExchange.getIn().setHeader("IsBody", false);

        Exchange bodyExchange = new DefaultExchange(new DefaultCamelContext());
        bodyExchange.getIn().setBody("{\"name\": \"John Doe\", \"age\": 30}");
        bodyExchange.getIn().setHeader("IsBody", true);

        // Act
        Exchange result = strategy.aggregate(headerExchange, bodyExchange);

        // Assert
        assertNotNull(result);
        assertEquals("{\"name\": \"John Doe\", \"age\": 30}", result.getIn().getBody(String.class));
        assertEquals("router1", result.getIn().getHeader("node-id"));

        System.out.println("Body is " + result.getIn().getBody(String.class) + "\n and header: " + result.getIn().getHeader("node-id"));

    }
}
