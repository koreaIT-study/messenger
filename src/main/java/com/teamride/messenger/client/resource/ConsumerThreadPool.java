package com.teamride.messenger.client.resource;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.springframework.stereotype.Component;

@Component
public class ConsumerThreadPool {
	private static ExecutorService executorService = Executors.newCachedThreadPool();

	public static ExecutorService getExecutorService() {
		return executorService;
	}
}
