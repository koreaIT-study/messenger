package com.teamride.messenger.client.utils;

import org.springframework.context.ApplicationContext;

import com.teamride.messenger.client.config.ApplicationContextProvider;

public class BeanUtils {
    public static <T> T getBean(Class<T> clazz) {
        ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();

        if( applicationContext == null ) {
            throw new NullPointerException("Spring의 ApplicationContext초기화 안됨");
        }
        return applicationContext.getBean(clazz);
    }
}
