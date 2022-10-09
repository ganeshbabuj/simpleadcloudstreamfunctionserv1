package com.example.simpleadcloudstreamfunctionserv1.stream;

import com.example.cloudstream.resource.AccountStatus;
import com.example.cloudstream.resource.Account;
import com.example.cloudstream.resource.AgeBand;
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
    public Function<KStream<Long, User>, KStream<Long, Account>> processAccount() {

        return userKStream -> {
            KStream<Long, Account> AccountKStream = userKStream.map((key, user) -> {
                Account account = activateAccount(user);
                System.out.println("key: " + key);
                System.out.println("user: " + user);
                System.out.println("account: " + account);
                return new KeyValue<>(key, account);
            });
            return AccountKStream;
        };
    }

    private Account activateAccount(User user) {

        AgeBand ageBand = AgeBand.ELDERLY;
        if(user.getAge() < 18) {
            ageBand = AgeBand.MINOR;
        } else if (user.getAge() < 45) {
            ageBand = AgeBand.YOUNG;
        } else if (user.getAge() < 60) {
            ageBand = AgeBand.MID_AGE;
        }

        if(user.getAge() >= 18) {
            return new Account(Instant.now().toEpochMilli(), user.getId(), ageBand, AccountStatus.ACTIVATED);
        } else {
            return new Account(Instant.now().toEpochMilli(), user.getId(), ageBand, AccountStatus.REJECTED);
        }
    }
}
