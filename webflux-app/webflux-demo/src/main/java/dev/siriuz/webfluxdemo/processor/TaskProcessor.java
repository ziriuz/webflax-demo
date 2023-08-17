package dev.siriuz.webfluxdemo.processor;

import dev.siriuz.webfluxdemo.dto.Response;
import dev.siriuz.webfluxdemo.util.SleepUtil;
import org.springframework.boot.CommandLineRunner;
import reactor.core.publisher.Flux;

import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

//@Component
public class TaskProcessor implements CommandLineRunner {

    Executor taskExecutor = Executors.newFixedThreadPool(3);

    private Runnable process(Response response) {
        return () ->{
            System.out.println(new Date() + " [" + Thread.currentThread().getName() + "] taskExecutor start " + response);
            SleepUtil.sleepSeconds((int) (Math.random() * 2));
            System.out.println(new Date() + " [" + Thread.currentThread().getName() + "] taskExecutor end   " + response);
        };
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("START commandLine ");
        Flux.range(1,12)
                //.delayElements(Duration.ofMillis(300))
                .map(Response::new)
                .doOnNext(response -> System.out.println(new Date() + " [" + Thread.currentThread().getName() + "] Task submitted:: " + response))
                .subscribe(response ->
                        taskExecutor.execute(process(response))
                );
        System.out.println("END commandLine ");
        //SleepUtil.sleepSeconds(20);
    }
}
