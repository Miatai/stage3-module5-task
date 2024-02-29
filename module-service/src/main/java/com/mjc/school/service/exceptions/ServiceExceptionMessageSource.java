package com.mjc.school.service.exceptions;

import org.springframework.context.MessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;

public class ServiceExceptionMessageSource {
    private static MessageSource messageSource;

    private ServiceExceptionMessageSource() {
    }

    public static MessageSource getMessageSource(){
        if(messageSource == null){
            ResourceBundleMessageSource source = new ResourceBundleMessageSource();
            source.setBasename("messages.ServiceExceptionMessages");
            source.setUseCodeAsDefaultMessage(true);
            source.setDefaultEncoding("UTF-8");
            messageSource = source;
        }
        return messageSource;
    }
}
