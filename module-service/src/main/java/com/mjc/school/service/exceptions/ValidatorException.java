package com.mjc.school.service.exceptions;

import java.util.Locale;

public class ValidatorException extends ServiceException {
    private final String constraintExceptionMessageCode = "000014";

    public ValidatorException(ServiceErrorCode serviceErrorCode, final String[] details) {
        super(serviceErrorCode, details);
    }

    @Override
    public String getLocalizedMessage(Locale locale) {
        StringBuilder messageSB = new StringBuilder(messageSource.getMessage("message." + serviceErrorCode.getErrorCode(),new Object[]{}, locale));
        String constraintMessage = messageSource.getMessage("message." + constraintExceptionMessageCode,new Object[]{}, locale);
        for(int i = 0; i<cause.length; i=i+3){
            messageSB.append(String.format(constraintMessage, new Object[]{cause[i], cause[i+1], cause[i+2]}));
        }
        return messageSB.toString();
    }
}
