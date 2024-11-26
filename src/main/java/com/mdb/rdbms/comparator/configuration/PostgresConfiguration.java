package com.mdb.rdbms.comparator.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;

/*@Configuration
@PropertySource({ "classpath:application.yml" })
@EnableJpaRepositories(
        basePackages = "com.mdb.rdbms.comparator.repositories.jpa",
        entityManagerFactoryRef = "pgEntityManager",
        transactionManagerRef = "pgTransactionManager"
)*/
public class PostgresConfiguration {

    @Value("${spring.jpa.properties.hibernate.ddl-auto}")
    String ddlAuto;
    @Value("${spring.jpa.properties.hibernate.dialect}")
    String dialect;
    @Value("${spring.datasource.driver-class-name}")
    String className;
    @Value("${spring.datasource.url}")
    String url;
    @Value("${spring.datasource.username}")
    String user;
    @Value("${spring.datasource.password}")
    String pass;

//
//
//    @Bean
//    @Primary
//    public LocalContainerEntityManagerFactoryBean pgEntityManager() {
//        LocalContainerEntityManagerFactoryBean em
//                = new LocalContainerEntityManagerFactoryBean();
//        em.setDataSource(pgDataSource());
//        em.setPackagesToScan(
//                new String[] { "com.mdb.rdbms.comparator.model" });
//
//        HibernateJpaVendorAdapter vendorAdapter
//                = new HibernateJpaVendorAdapter();
//        em.setJpaVendorAdapter(vendorAdapter);
//        HashMap<String, Object> properties = new HashMap<>();
//        properties.put("hibernate.hbm2ddl.auto", ddlAuto);
//        properties.put("hibernate.dialect", dialect);
//        em.setJpaPropertyMap(properties);
//
//        return em;
//    }
//
//    @Primary
//    @Bean
//    public DataSource pgDataSource() {
//
//        DriverManagerDataSource dataSource
//                = new DriverManagerDataSource();
//        dataSource.setDriverClassName(className);
//        dataSource.setUrl(url);
//        dataSource.setUsername(user);
//        dataSource.setPassword(pass);
//
//        return dataSource;
//    }
//
//    @Primary
//    @Bean
//    public PlatformTransactionManager pgTransactionManager() {
//
//        JpaTransactionManager transactionManager
//                = new JpaTransactionManager();
//        transactionManager.setEntityManagerFactory(
//                pgEntityManager().getObject());
//        return transactionManager;
//    }

}
