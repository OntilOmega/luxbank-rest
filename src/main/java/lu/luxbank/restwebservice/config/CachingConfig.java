package lu.luxbank.restwebservice.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableCaching
@EnableScheduling
@Log4j2
public class CachingConfig {

    @Scheduled(cron = "0 0 0 * * *")
    @CacheEvict(value = "openIban", allEntries = true)
    public void evictOpenIbanCachesAtMidnight() {
        log.info("openIban cache has been evicted");
    }
}
