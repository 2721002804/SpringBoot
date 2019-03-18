package com.dd.o2o.config.dao;

import java.beans.PropertyVetoException;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mchange.v2.c3p0.ComboPooledDataSource;

@Configuration
@MapperScan("com.dd.o2o.dao")
public class DataSourceConfiguration {
	@Value("${jdbc.driver}")
	private String jdbcDriver;
	@Value("${jdbc.url}")
	private String jdbcUrl;
	@Value("${jdbc.username}")
	private String jdbcUsername;
	@Value("${jdbc.password}")
	private String jdbcPassword;
	@Bean(name="dataSource")
	public ComboPooledDataSource createDataSource() throws PropertyVetoException {
		ComboPooledDataSource dataSource=new ComboPooledDataSource();
		dataSource.setDriverClass(jdbcDriver);
		dataSource.setJdbcUrl(jdbcUrl);
		dataSource.setUser(jdbcUsername);
		dataSource.setPassword(jdbcPassword);
		dataSource.setMaxPoolSize(30);
		dataSource.setMinPoolSize(10);
		dataSource.setAutoCommitOnClose(false);
		dataSource.setCheckoutTimeout(10000);
		dataSource.setAcquireRetryAttempts(2);
		return dataSource;
	}
}
