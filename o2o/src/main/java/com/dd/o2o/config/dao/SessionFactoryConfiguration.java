package com.dd.o2o.config.dao;

import java.io.IOException;

import javax.sql.DataSource;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

@Configuration
public class SessionFactoryConfiguration {
	
	private static String mybatisConfigFile;
	
	@Value("${mybatis_config_file}")
	public void setMybatisConfigFile(String mybatisConfigFile) {
		SessionFactoryConfiguration.mybatisConfigFile = mybatisConfigFile;
	}

	
	private static String mapperPath;
	
	@Value("${mapper_path}")
	public void setMapperPath(String mapperPath) {
		SessionFactoryConfiguration.mapperPath = mapperPath;
	}


	@Value("${type_alias_package}")
	private String typeAliasesPackage;
	
	
	@Autowired
	private DataSource dataSource;
	
	
	@Bean(name="sqlSessionFactory")
	public SqlSessionFactoryBean createSqlSessionFactoryBean() throws IOException {
		SqlSessionFactoryBean sqlSessionFactoryBean=new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setConfigLocation(new ClassPathResource(mybatisConfigFile));
		//添加mapper扫描路径
		PathMatchingResourcePatternResolver pathMatchingResourcePatternResolver=new PathMatchingResourcePatternResolver();
		String packageSerachPath=ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX+mapperPath;
		sqlSessionFactoryBean.setMapperLocations(pathMatchingResourcePatternResolver.getResources(packageSerachPath));
		sqlSessionFactoryBean.setDataSource(dataSource);
		sqlSessionFactoryBean.setTypeAliasesPackage(typeAliasesPackage);
		return sqlSessionFactoryBean;
	}
}
