package com.example.ProsePetal.Repositories;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class TokenRepo {
    // Assuming your token repository stores objects in a map based on the token
    private Map<String, Object> tokenMap;

    public TokenRepo() {
        this.tokenMap = new HashMap<>();
    }

    // Method to add an object to the token repository
    public void addObject(String jwt, Object object) {
        tokenMap.put(jwt, object);
    }

    // Method to find an object based on the token
    public <T> Optional<T> findByToken(String jwt) {
        // Retrieve the object from the map based on the token
        Object foundObject = tokenMap.get(jwt);

        // Check if the object is found
        if (foundObject != null) {
            // If found, cast the object to the expected type and return it wrapped in an Optional
            @SuppressWarnings("unchecked")
            T typedObject = (T) foundObject;
            return Optional.of(typedObject);
        } else {
            // If not found, return an empty Optional
            return Optional.empty();
        }
    }
}
