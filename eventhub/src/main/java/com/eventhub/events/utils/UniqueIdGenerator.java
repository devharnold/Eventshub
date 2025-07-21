package com.eventhub.events.utils;

import java.util.UUID;

//public class UniqueIdGenerator {
//    public static void main(String[] args) {
//        UUID uniqueId = UUID.randomUUID();
//
//        // Convert the UUID to a String
//        String uniqueIdAsString = uniqueId.toString();
//
//        System.out.println(uniqueIdAsString);
//    }
//}

public class UniqueIdGenerator {
    public static String generateUniqueId() {
        return UUID.randomUUID().toString().substring(0, 5);
    }
}