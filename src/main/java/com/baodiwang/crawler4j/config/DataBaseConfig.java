//package com.baodiwang.crawler4j.config;
//
//import com.alibaba.druid.pool.DruidDataSource;
//import org.apache.ibatis.session.SqlSessionFactory;
//import org.mybatis.spring.SqlSessionFactoryBean;
//import org.mybatis.spring.annotation.MapperScan;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
//import org.springframework.jdbc.datasource.DataSourceTransactionManager;
//import org.springframework.transaction.PlatformTransactionManager;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//
//import javax.sql.DataSource;
//import java.sql.SQLException;
//import java.util.Properties;
//
///**
// * Created by lizhou on 2015/12/11
// */
//
//@Configuration
//@EnableTransactionManagement
//@MapperScan("com.baodiwang.crawler4j.mapper")
//@EnableAutoConfiguration
//public class DataBaseConfig {
//
//    private final Logger log = LoggerFactory.getLogger(DataBaseConfig.class);
//
//    @Value("${spring.datasource.driverClassName}")
//    private String driver;
//
//    @Value("${spring.datasource.url}")
//    private String url;
//
//    @Value("${spring.datasource.username}")
//    private String username;
//
//    @Value("${spring.datasource.password}")
//    private String password;
//
//    @Value("${spring.datasource.initialSize}")
//    private int initialSize;
//
//    @Value("${spring.datasource.minIdle}")
//    private int minIdle;
//
//    @Value("${spring.datasource.maxActive}")
//    private int maxActive;
//
//    @Value("${spring.datasource.maxWait}")
//    private int maxWait;
//
//    @Value("${spring.datasource.timeBetweenEvictionRunsMillis}")
//    private int timeBetweenEvictionRunsMillis;
//
//    @Value("${spring.datasource.minEvictableIdleTimeMillis}")
//    private int minEvictableIdleTimeMillis;
//
//    @Value("${spring.datasource.validateQuery}")
//    private String validateQuery;
//
//    @Value("${spring.datasource.testWhileIdle}")
//    private boolean testWhileIdle;
//
//    @Value("${spring.datasource.testOnBorrow}")
//    private boolean testOnBorrow;
//
//    @Value("${spring.datasource.testOnReturn}")
//    private boolean testOnReturn;
//
//    @Value("${spring.datasource.poolPreparedStatements}")
//    private boolean poolPreparedStatements;
//
//    @Value("${spring.datasource.maxPoolPreparedStatementPerConnectionSize}")
//    private int maxPoolPreparedStatementPerConnectionSize;
//
//    @Value("${spring.datasource.filters}")
//    private String filters;
//
//    @Value("${druid.stat.config_decrypt}")
//    private String configDecrypt;
//
//    @Value("${druid.stat.config_decrypt_publickey}")
//    private String configDecryptPublicKey;
//
//    @Value("${druid.stat.mergeSql}")
//    private String mergeSql;
//
//    @Value("${druid.stat.slowSqlMillis}")
//    private int slowSqlMillis;
//
//
//
//
//    @Bean
//    @Primary
//    @ConfigurationProperties(prefix = "jdbc")
//    public DataSource dataSource() {
//        log.debug("Configuring Datasource");
//        DruidDataSource druidDataSource = new DruidDataSource();
//        druidDataSource.setDriverClassName(this.driver);
//        druidDataSource.setUrl(this.url);
//        druidDataSource.setUsername(this.username);
//        druidDataSource.setPassword(this.password);
//        druidDataSource.setInitialSize(this.initialSize);
//        druidDataSource.setMinIdle(this.minIdle);
//        druidDataSource.setMaxActive(this.maxActive);
//        druidDataSource.setMaxWait(this.maxWait);
//        druidDataSource.setTimeBetweenEvictionRunsMillis(this.timeBetweenEvictionRunsMillis);
//        druidDataSource.setMinEvictableIdleTimeMillis(this.minEvictableIdleTimeMillis);
//        druidDataSource.setValidationQuery(this.validateQuery);
//        druidDataSource.setTestWhileIdle(this.testWhileIdle);
//        druidDataSource.setTestOnBorrow(this.testOnBorrow);
//        druidDataSource.setTestOnReturn(this.testOnReturn);
//        druidDataSource.setPoolPreparedStatements(this.poolPreparedStatements);
//        druidDataSource.setMaxPoolPreparedStatementPerConnectionSize(this.maxPoolPreparedStatementPerConnectionSize);
//        try {
//            druidDataSource.setFilters(this.filters);
//        } catch (SQLException e) {
//            e.printStackTrace();
//            log.error("Druid configuration initialization filter failed ...",e);
//        }
//        Properties properties = new Properties();
//        properties.setProperty("druid.stat.mergeSql",this.mergeSql);
//        properties.setProperty("druid.stat.slowSqlMillis",this.slowSqlMillis+"");
//        properties.setProperty("config.decrypt",this.configDecrypt);
//        properties.setProperty("config.decrypt.key",this.configDecryptPublicKey);
//        druidDataSource.setConnectProperties(properties);
//        return druidDataSource;
//    }
//
//    @Bean
//    @Primary
//    public PlatformTransactionManager txManager() {
//        return new DataSourceTransactionManager(dataSource());
//    }
//
//    @Bean
//    public SqlSessionFactory sqlSessionFactoryBean() throws Exception {
//
//        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
//        sqlSessionFactoryBean.setDataSource(dataSource());
//        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
//
//        sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath:/mybatis/mapper/*.xml"));
//        return sqlSessionFactoryBean.getObject();
//    }
//
//
//}
