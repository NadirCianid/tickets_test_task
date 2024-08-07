package org.cianid.nadir.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.time.LocalDate;
import java.time.LocalTime;

public class Ticket {
    @JsonProperty("origin")
    private String origin;

    @JsonProperty("origin_name")
    private String originName;

    @JsonProperty("destination")
    private String destination;

    @JsonProperty("destination_name")
    private String destinationName;

    @JsonProperty("departure_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yy")
    private LocalDate departureDate;

    @JsonProperty("departure_time")
    @JsonDeserialize(using = CustomLocalTimeDeserializer.class)
    private LocalTime departureTime;

    @JsonProperty("arrival_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yy")
    private LocalDate arrivalDate;

    @JsonProperty("arrival_time")
    @JsonDeserialize(using = CustomLocalTimeDeserializer.class)
    private LocalTime arrivalTime;

    @JsonProperty("carrier")
    private String carrier;

    @JsonProperty("stops")
    private int stops;

    @JsonProperty("price")
    private int price;


    public Ticket() {}

    public Ticket(
            String origin,
            String originName,
            String destination,
            String destinationName,
            LocalDate departureDate,
            LocalTime departureTime,
            LocalDate arrivalDate,
            LocalTime arrivalTime,
            String carrier,
            int stops,
            int price) {
        this.origin = origin;
        this.originName = originName;
        this.destination = destination;
        this.destinationName = destinationName;
        this.departureDate = departureDate;
        this.departureTime = departureTime;
        this.arrivalDate = arrivalDate;
        this.arrivalTime = arrivalTime;
        this.carrier = carrier;
        this.stops = stops;
        this.price = price;
    }

    public String getOrigin() {
        return origin;
    }

    public String getOriginName() {
        return originName;
    }

    public String getDestination() {
        return destination;
    }

    public String getDestinationName() {
        return destinationName;
    }

    public LocalDate getDepartureDate() {
        return departureDate;
    }

    public LocalTime getDepartureTime() {
        return departureTime;
    }

    public LocalDate getArrivalDate() {
        return arrivalDate;
    }

    public LocalTime getArrivalTime() {
        return arrivalTime;
    }

    public String getCarrier() {
        return carrier;
    }

    public int getStops() {
        return stops;
    }

    public int getPrice() {
        return price;
    }
}