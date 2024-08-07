package org.cianid.nadir;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.cianid.nadir.model.Ticket;
import org.cianid.nadir.model.TicketsList;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class Main {
    public static final String TICKETS_JSON_PATH = "src/main/resources/tickets.json";

    public static void main(String[] args) {
        TicketsList ticketsList = readTicketsJson();

        if (ticketsList == null) {
            System.out.println("Failed to read json");
            return;
        }


        var ticketsByCarrier = ticketsList.getTickets().stream()
                .filter(Main::isBetweenVVOAndTLV)
                .collect(Collectors.groupingBy(Ticket::getCarrier));


        Map<String, Long> minFlightDurationsByCarrier = new HashMap<>();

        for (Map.Entry<String, List<Ticket>> entry : ticketsByCarrier.entrySet()) {
            Optional<Long> minDuration = calculateMinFlightTime(entry);

            minFlightDurationsByCarrier.put(entry.getKey(), minDuration.orElse(-1L));
        }


        List<Integer> ticketsPrices = ticketsByCarrier.entrySet().stream()
                .flatMap(entry -> entry.getValue().stream().map(Ticket::getPrice))
                .toList();

        BigDecimal meanPriceAndMedianDiff = calculateMeanPriceAndMedianDiff(ticketsPrices);


        System.out.println("Minimum flight time between the cities of Vladivostok and Tel Aviv for each air carrier (in minutes):");
        System.out.println(minFlightDurationsByCarrier);

        System.out.println();

        System.out.println("The difference between the average price and the median for a flight between the cities of Vladivostok and Tel Aviv:");
        System.out.println(meanPriceAndMedianDiff);
    }

    private static BigDecimal calculateMeanPriceAndMedianDiff(List<Integer> prices) {
        double mean = calculateMean(prices);
        double median = calculateMedian(prices);

        double diff = Math.abs(mean - median);

        BigDecimal bd = BigDecimal.valueOf(diff);
        return bd.setScale(2, RoundingMode.HALF_UP);
    }

    public static double calculateMean(List<Integer> prices) {
        return prices.stream()
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0.0);
    }

    public static double calculateMedian(List<Integer> prices) {
        List<Integer> sortedPrices = prices.stream()
                .sorted().toList();

        int size = sortedPrices.size();
        if (size % 2 == 0) {
            // Если количество элементов четное, медиана - это среднее значение двух центральных элементов
            return (sortedPrices.get(size / 2 - 1) + sortedPrices.get(size / 2)) / 2.0;
        } else {
            // Если количество элементов нечетное, медиана - это средний элемент
            return sortedPrices.get(size / 2);
        }
    }

    private static Optional<Long> calculateMinFlightTime(Map.Entry<String, List<Ticket>> entry) {
        return entry.getValue().stream()
                .map(ticket -> {
                    LocalDateTime departureTime = ticket.getDepartureDate().atTime(ticket.getDepartureTime());
                    LocalDateTime arrivalTime = ticket.getArrivalDate().atTime(ticket.getArrivalTime());

                    return ChronoUnit.MINUTES.between(departureTime, arrivalTime);
                })
                .min(Comparator.naturalOrder());
    }

    private static boolean isBetweenVVOAndTLV(Ticket ticket) {
        return ticket.getOrigin().equals("VVO") && ticket.getDestination().equals("TLV")
                || (ticket.getOrigin().equals("TLV") && ticket.getDestination().equals("VVO"));
    }

    private static TicketsList readTicketsJson() {
        File jsonFile = new File(TICKETS_JSON_PATH);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        try {
            return objectMapper.readValue(jsonFile, TicketsList.class);
        } catch (IOException e) {
            return null;
        }
    }
}