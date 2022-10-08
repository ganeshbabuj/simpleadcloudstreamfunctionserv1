package com.example.simpleadcloudstreamfunctionserv1.stream;

import com.example.cloudstream.resource.AccountStatus;
import com.example.cloudstream.resource.Account;
import com.example.cloudstream.resource.User;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

import java.time.Instant;
import java.util.function.Function;

@Configuration
public class AccountKafkaProcessor {


    @Bean
    public Function<Flux<User>, Flux<Account>> processAccount1() {
        return userFlux -> userFlux.map(user -> {
                    Account account = activateAccount(user);
                    System.out.println("user: " + user);
                    System.out.println("account: " + account);
                    return account;
                }).log();
    }

    @Bean
    public Function<KStream<String, User>, KStream<String, Account>> processAccount() {

        return userKStream -> {
            KStream<String, Account> AccountKStream = userKStream.map((key, user) -> {
                Account account = activateAccount(user);
                System.out.println("user: " + user);
                System.out.println("account: " + account);
                return new KeyValue<>(key, account);
            });
            return AccountKStream;
        };
    }

    private Account activateAccount(User user) {

        if(user.getAge() > 18) {
            return new Account(Instant.now().toEpochMilli(), user.getId(), AccountStatus.ACTIVATED);
        } else {
            return new Account(Instant.now().toEpochMilli(), user.getId(), AccountStatus.REJECTED);
        }
    }
}
