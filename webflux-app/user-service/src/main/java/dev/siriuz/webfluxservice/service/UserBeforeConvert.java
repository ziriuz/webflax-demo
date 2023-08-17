package dev.siriuz.webfluxservice.service;

import dev.siriuz.webfluxservice.entity.User;
import org.reactivestreams.Publisher;
import org.springframework.data.r2dbc.mapping.event.BeforeConvertCallback;
import org.springframework.data.relational.core.sql.SqlIdentifier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class UserBeforeConvert implements BeforeConvertCallback<User> {

    private static final String PATTERN = "[^A-Za-z]";
    @Override
    public Publisher<User> onBeforeConvert(User userEntity, SqlIdentifier table) {
        String username = userEntity.getName().replaceAll(PATTERN,"");
        System.out.println("Actual: " + userEntity.getName());
        System.out.println("Updated: " + username);
        userEntity.setName(username);
        return Mono.just(userEntity);
    }
}
