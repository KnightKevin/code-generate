package com.codegenerator.app;

import com.codegenerator.app.config.BatchConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.batch.core.configuration.annotation.BatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@MapperScan("com.codegenerator.app.mapper")
@EnableBatchProcessing
@SpringBootApplication
public class AppApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppApplication.class, args);
	}

	@Bean
	public BatchConfigurer batchConfigurer() {
		return new BatchConfig();
	}
}
