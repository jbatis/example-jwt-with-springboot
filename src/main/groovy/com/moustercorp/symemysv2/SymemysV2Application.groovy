package com.moustercorp.symemysv2

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

//@Configuration
//@EnableAutoConfiguration
//@ComponentScan(basePackages = "com.moustercorp.symemys")

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan(basePackages = "com.moustercorp")
class SymemysV2Application {

	static void main(String[] args) {
		SpringApplication.run SymemysV2Application, args
	}
}
