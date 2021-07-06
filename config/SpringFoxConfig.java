/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.hateoas.client.LinkDiscoverer;
import org.springframework.hateoas.client.LinkDiscoverers;
import org.springframework.hateoas.mediatype.collectionjson.CollectionJsonLinkDiscoverer;
import org.springframework.plugin.core.SimplePluginRegistry;

import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 *
 * @author emmanuel.mfikwa
 */
@Configuration
@EnableSwagger2
public class SpringFoxConfig {

//	@Bean
//	public Docket api() {
//
//		ParameterBuilder paramBuilder = new ParameterBuilder();
//		List<Parameter> params = new ArrayList<>();
//		paramBuilder.name("Authorization").modelRef(new ModelRef("string")).parameterType("header").required(false)
//				.build();
//
//		params.add(paramBuilder.build());
//
//		return new Docket(DocumentationType.SWAGGER_2).groupName("user-api").useDefaultResponseMessages(false)
//				.globalOperationParameters(params).select().apis(RequestHandlerSelectors.any())
//				.paths(PathSelectors.any()).build().apiInfo(apiInfo())
//				.produces(new HashSet<String>(Arrays.asList("application/json")))
//				.consumes(new HashSet<String>(Arrays.asList("application/json")));
//	}
//
//	private ApiInfo apiInfo() {
//		return new ApiInfo("Licensing Platform REST API",
//				"An API for all communications to all services in exposed by the platform.", "API REFERENCE",
//				"Terms of service", new Contact("TCRA Developers", "www.tcra.go.tz", "developers@tcra.go.tz"),
//				"License of API", "API license URL", Collections.emptyList());
//	}

	@Primary
	@Bean
	public LinkDiscoverers discoverers() {
		List<LinkDiscoverer> plugins = new ArrayList<>();
		plugins.add(new CollectionJsonLinkDiscoverer());
		return new LinkDiscoverers(SimplePluginRegistry.create(plugins));
	}

	@Bean
	public Docket api() {
		ParameterBuilder paramBuilder = new ParameterBuilder();
		List<Parameter> params = new ArrayList<>();
		paramBuilder.name("Authorization").modelRef(new ModelRef("string")).parameterType("header").required(false)
				.build();
		params.add(paramBuilder.build());
		return new Docket(DocumentationType.SWAGGER_2).groupName("lms-api").useDefaultResponseMessages(false)
				.globalOperationParameters(params).select().apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.any()).build().apiInfo(apiInfo())
				.consumes(new HashSet<String>(Arrays.asList("application/json")))
				.produces(new HashSet<String>(Arrays.asList("application/json")));
	}

	private ApiInfo apiInfo() {
		return new ApiInfo("LMS BACK END REST API", "Some custom description of API.", "API REFERENCE",
				"Terms of service", new Contact("TCRA Developers", "www.tcra.go.tz", "developers@tcra.go.tz"),
				"License of API", "API license URL", Collections.emptyList());
	}

}
