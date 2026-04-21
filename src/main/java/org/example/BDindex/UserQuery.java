package org.example.BDindex;

public record UserQuery(
        Long id,
        Integer age,
        String name,
        Integer ageFrom,
        Integer ageTo,
        Boolean active
) {
}
