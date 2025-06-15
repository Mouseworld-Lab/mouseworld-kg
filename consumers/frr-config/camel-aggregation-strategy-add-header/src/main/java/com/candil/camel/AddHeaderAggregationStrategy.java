package com.candil.camel;

import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;

public class AddHeaderAggregationStrategy implements AggregationStrategy {
    private String bodyIdentifierHeader; // Header to identify the exchange containing the body
    private String headerName;          // Header name to extract from the second exchange

    // No-argument constructor
    public AddHeaderAggregationStrategy() {
        this.bodyIdentifierHeader = "isBody"; // Provide a default value if needed
        this.headerName = "node-id"; // Provide a default value if needed
    }

    // Constructor to specify custom headers
    public AddHeaderAggregationStrategy(String bodyIdentifierHeader, String headerName) {
        this.bodyIdentifierHeader = bodyIdentifierHeader;
        this.headerName = headerName;
    }

    // Getter and Setter for bodyIdentifierHeader
    public String getBodyIdentifierHeader() {
        return bodyIdentifierHeader;
    }

    public void setBodyIdentifierHeader(String bodyIdentifierHeader) {
        this.bodyIdentifierHeader = bodyIdentifierHeader;
    }

    // Getter and Setter for headerName
    public String getHeaderName() {
        return headerName;
    }

    public void setHeaderName(String headerName) {
        this.headerName = headerName;
    }

    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        if (oldExchange == null) {
            return newExchange;
        }

        // Check if newExchange is the one containing the body
        Boolean newExchangeIsBody = newExchange.getIn().getHeader(bodyIdentifierHeader, Boolean.class);
        if (Boolean.TRUE.equals(newExchangeIsBody)) {
            // Use newExchange's body as the main body
            oldExchange.getIn().setBody(newExchange.getIn().getBody(String.class));
        } else {
            // Use the specified header from newExchange and set it in oldExchange
            String headerValue = newExchange.getIn().getHeader(headerName, String.class);
            oldExchange.getIn().setHeader(headerName, headerValue);
        }

        return oldExchange;
    }
}
