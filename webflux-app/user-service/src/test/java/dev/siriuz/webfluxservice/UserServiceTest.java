package dev.siriuz.webfluxservice;

import dev.siriuz.webfluxservice.dto.UserDto;
import dev.siriuz.webfluxservice.entity.User;
import dev.siriuz.webfluxservice.repository.UserRepository;
import dev.siriuz.webfluxservice.service.UserService;
import dev.siriuz.webfluxservice.util.EntityDtoUtil;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest
public class UserServiceTest {

    //@MockBean
    //UserRepository repository;

    @Autowired
    UserService service;
    @Test
    public void createUserTest(){

        var i = 0;

        UserDto userDto = new UserDto();
        userDto.setName("testuser***");

        //Mockito.when(repository.save(Mockito.any()))
        //        .thenReturn(Mono.just(EntityDtoUtil.toEntity(userDto)));

        Mono<UserDto> mono = service.createUser(Mono.just(userDto))
                .doOnNext(System.out::println);

        StepVerifier.create(mono)
                .expectNextCount(1)
                .verifyComplete();

    }
}
