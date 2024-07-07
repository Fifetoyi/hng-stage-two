package com.fifetoyi.hng_stage_two;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
@EntityScan("com.fifetoyi.hng_stage_two.model")
public class HngStageTwoApplication {

    public static void main(final String[] args) {
        SpringApplication.run(HngStageTwoApplication.class, args);
    }

}
