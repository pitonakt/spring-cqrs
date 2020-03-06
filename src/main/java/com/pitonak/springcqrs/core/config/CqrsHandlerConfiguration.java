package com.pitonak.springcqrs.core.config;

import java.util.Map;

import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.pitonak.springcqrs.core.command.CommandHandler;
import com.pitonak.springcqrs.core.utils.CqrsUtils;

@Configuration
public class CqrsHandlerConfiguration implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {

        final RequestMappingHandlerMapping mapping = new RequestMappingHandlerMapping();
        mapping.setOrder(Integer.MAX_VALUE - 2);

        final Map<String, CommandHandler> handlers = beanFactory.getBeansOfType(CommandHandler.class);

        handlers.entrySet()
                .stream()
                .map(Map.Entry::getValue)
                .forEach(handler -> {
                    RequestMappingInfo requestMappingInfo = RequestMappingInfo.paths(CqrsUtils.determineUrlsForHandler(handler))
                            .methods(CqrsUtils.determineRequestMethodsForHandler(handler))
                            .produces(MediaType.APPLICATION_JSON_VALUE)
                            .build();

                    mapping.registerMapping(requestMappingInfo,
                            handler,
                            handler.getClass()
                                    .getMethods()[0]);
                });

        beanFactory.registerSingleton("requestMappingHandlerMapping",
                mapping);
    }
}
