package com.zd.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = "com.zd.dao.stock", sqlSessionFactoryRef = "stockSqlSessionFactory")
public class stockShareConfig {

    @Bean(name = "stockDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.StockShare")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "stockTransactionManager")
    public DataSourceTransactionManager transactionManager(@Qualifier("stockDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);

    }

    @Bean(name = "stockSqlSessionFactory")
    public SqlSessionFactory basicSqlSessionFactory(@Qualifier("stockDataSource") DataSource basicDataSource) throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(basicDataSource);
        factoryBean.setMapperLocations(
                new PathMatchingResourcePatternResolver().getResources("classpath:mapper/stock/*.xml"));
        return factoryBean.getObject();
    }

    @Bean(name = "stockSqlSessionTemplate")
    public SqlSessionTemplate testSqlSessionTemplate(
            @Qualifier("stockSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
