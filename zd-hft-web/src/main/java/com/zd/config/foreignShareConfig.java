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
@MapperScan(basePackages = "com.zd.dao.foreign", sqlSessionFactoryRef = "foreignSqlSessionFactory")
public class foreignShareConfig {

    @Bean(name = "foreignDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.ForeignShare")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "foreignTransactionManager")
    public DataSourceTransactionManager transactionManager(@Qualifier("foreignDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);

    }

    @Bean(name = "foreignSqlSessionFactory")
    public SqlSessionFactory basicSqlSessionFactory(@Qualifier("foreignDataSource") DataSource basicDataSource) throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(basicDataSource);
        factoryBean.setMapperLocations(
                new PathMatchingResourcePatternResolver().getResources("classpath:mapper/foreign/*.xml"));
        return factoryBean.getObject();
    }

    @Bean(name = "foreignSqlSessionTemplate")
    public SqlSessionTemplate testSqlSessionTemplate(
            @Qualifier("foreignSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
