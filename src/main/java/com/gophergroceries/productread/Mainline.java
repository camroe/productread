package com.gophergroceries.productread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import com.gophergroceries.productread.product.ProductReader;
import com.gophergroceries.productread.test.ProductTest;

@Configuration
// @EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class, })
@EnableAutoConfiguration 
@EnableJpaRepositories
@ComponentScan
@Component
/**
 * The @SpringBootApplication annotation is equivalent to using @Configuration,
 * 
 * @EnableAutoConfiguration and @ComponentScan with their default attributes:
 * 
 * @author camroe
 *
 */
public class Mainline {

	private static final Logger logger = LoggerFactory.getLogger(Mainline.class);
	private static String projectName;
	private static ProductTest productTest;
	private static ProductReader productReader;

	/**
	 * Usually the class that defines the main method is also a good candidate as
	 * the primary @Configuration.
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		SpringApplication.run(Mainline.class, args);
		System.out.println("Hello Worlds! Thi is is " + projectName);
		logger.trace("This is an example {} message", "Trace");
		logger.debug("Product Reader  is {}", productReader == null ? " NULL" : " NOT NULL");
		logger.debug("Product Test  is {}", productTest == null ? " NULL" : " NOT NULL");
		productReader.readProducts();
		productTest.printProducts();
		System.exit(0);
	}

	/**
	 * Example of how to autowire a STATIC variable on the setter. It should be
	 * noted that this is an anti-pattern and should not be used but nice to know
	 * how.
	 * 
	 * @param projectName
	 */
	@Value("${configuration.projectName}")
	public void setProjectName(String projectName) {
		Mainline.projectName = projectName;
	}

	@Autowired
	public void setProductTest(ProductTest pt) {
		Mainline.productTest = pt;
	}

	@Autowired
	public void setProductReader(ProductReader pr) {
		Mainline.productReader = pr;
	}
}
