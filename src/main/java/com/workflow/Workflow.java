package com.workflow;
import org.activiti.spring.boot.SecurityAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;
@EnableScheduling
@EnableAspectJAutoProxy
@SpringBootApplication (exclude = { SecurityAutoConfiguration.class})
public class Workflow {

	public static void main(String[] args) {
		SpringApplication.run(Workflow.class, args);
		
	}

}
