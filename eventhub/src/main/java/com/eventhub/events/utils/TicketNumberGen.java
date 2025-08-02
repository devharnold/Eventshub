package com.eventhub.events.utils;

import java.util.UUID;

// generate a number then slice the string to pick the first 5 characters
public class TicketNumberGen {
    public static String generateTicketNumber() {
        return UUID.randomUUID().toString().substring(0, 7);
    }
}
