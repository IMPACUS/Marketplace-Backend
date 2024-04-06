package com.impacus.maketplace.utils;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

//@Component
public class DataLoaderBootstrap implements ApplicationListener<ContextRefreshedEvent> {
	private DataLoader dataLoader;

	public DataLoaderBootstrap(DataLoader dataLoader) {
		this.dataLoader = dataLoader;
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		dataLoader.loadData();
	}
}
