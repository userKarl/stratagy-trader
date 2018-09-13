
package com.zd.common.utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PreDestroy;

@Configuration
@EnableCaching
public class CacheConfiguration {
    private final Logger log = LoggerFactory.getLogger(CacheConfiguration.class);

    private net.sf.ehcache.CacheManager cacheManager;

    @PreDestroy
    public void destroy() {
        cacheManager.shutdown();
    }

    @Bean
    public CacheManager cacheManager() {
        log.debug("Starting Ehcache");
        cacheManager = net.sf.ehcache.CacheManager.create();
        cacheManager.getConfiguration().setMaxBytesLocalHeap("16M");
        EhCacheCacheManager ehCacheManager = new EhCacheCacheManager();
        ehCacheManager.setCacheManager(cacheManager);
        return ehCacheManager;
    }
}

