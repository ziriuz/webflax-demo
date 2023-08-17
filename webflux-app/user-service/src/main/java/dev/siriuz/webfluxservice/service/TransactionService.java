package dev.siriuz.webfluxservice.service;

import dev.siriuz.webfluxservice.dto.TransactionRequestDto;
import dev.siriuz.webfluxservice.dto.TransactionResponseDto;
import dev.siriuz.webfluxservice.dto.TransactionStatus;
import dev.siriuz.webfluxservice.entity.UserTransaction;
import dev.siriuz.webfluxservice.repository.UserRepository;
import dev.siriuz.webfluxservice.repository.UserTransactionRepository;
import dev.siriuz.webfluxservice.util.EntityDtoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TransactionService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserTransactionRepository transactionRepository;

    public Mono<TransactionResponseDto> createTransaction(final TransactionRequestDto requestDto){
        return this.userRepository.updateUserBalance(requestDto.getUserId(), requestDto.getAmount())
                        .filter(Boolean::booleanValue)
                        .map(b -> EntityDtoUtil.toEntity(requestDto))
                        .flatMap(this.transactionRepository::save)
                        .map(ut -> EntityDtoUtil.toDto(requestDto, TransactionStatus.APPROVED))
                        .defaultIfEmpty(EntityDtoUtil.toDto(requestDto, TransactionStatus.DECLINED));
    }

    public Flux<UserTransaction> getByUserId(int userId){
        return this.transactionRepository.findByUserId(userId);
    }

}
