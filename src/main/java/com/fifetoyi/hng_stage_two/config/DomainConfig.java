package com.fifetoyi.hng_stage_two.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Configuration
@EntityScan("com.fifetoyi.hng_stage_two.repos")
@EnableJpaRepositories(basePackages = "com.fifetoyi.hng_stage_two.repos")
@EnableTransactionManagement
public class DomainConfig {
}
