package com.mjc.school.controller.exception;

import org.springframework.context.MessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;

public class ControllerExceptionMessageSource {
    private static MessageSource messageSource;

    private ControllerExceptionMessageSource() {
    }

    public static MessageSource getMessageSource(){
        if(messageSource == null){
            ResourceBundleMessageSource source = new ResourceBundleMessageSource();
            source.setBasename("messages.ControllerExceptionMessages");
            source.setUseCodeAsDefaultMessage(true);
            source.setDefaultEncoding("UTF-8");
            messageSource = source;
        }
        return messageSource;
    }
}
