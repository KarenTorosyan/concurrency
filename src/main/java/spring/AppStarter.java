package spring;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.math.BigDecimal;

@SpringBootApplication
@EnableAsync
public class AppStarter {
    public static void main(String[] args) {
        new SpringApplicationBuilder()
                .bannerMode(Banner.Mode.OFF)
                .sources(AppStarter.class)
                .run(args);
    }

    @Bean
    ApplicationRunner doWork(AccountService accountService) {
        return args -> {
            Account bobAccount = accountService.registerAccount(new Account("Bob", BigDecimal.valueOf(200)));
            Account alisaAccount = accountService.registerAccount(new Account("Alisa", BigDecimal.valueOf(200)));
            accountService.transfer(bobAccount, alisaAccount, BigDecimal.valueOf(100));
            accountService.transfer(alisaAccount, bobAccount, BigDecimal.valueOf(100));
        };
    }

    @Primary
    @Bean
    TaskExecutor appThreadPool() {
        var executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(30);
        executor.setQueueCapacity(Integer.MAX_VALUE);
        executor.setThreadNamePrefix("app-thread-pool-");
        return executor;
    }
}
