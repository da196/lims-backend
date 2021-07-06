/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.config;

import java.util.concurrent.Executor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 *
 * @author emmanuel.mfikwa
 */

@Configuration

@EnableAsync

// @EnableSwagger2

public class LimsConfig {

	public static final String zone = "Africa/Dar_es_Salaam";
	public static final String sender = "no-reply@tcra.go.tz";

	@Value("${thread.core.pool.size}")
	private int corePoolSize;

	@Value("${thread.max.pool.size}")
	private int maxPoolSize;

	@Value("${thread.max.que.size}")
	private int maxQueSize;

	@Bean
	public Executor taskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(corePoolSize);
		executor.setMaxPoolSize(maxPoolSize);
		executor.setQueueCapacity(maxQueSize);
		executor.setThreadNamePrefix("ICHIS-");
		executor.initialize();
		return executor;
	}

}
