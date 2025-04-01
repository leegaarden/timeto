package com.timeto.config.exception.custom;

import com.timeto.config.exception.ErrorCode;
import com.timeto.config.exception.GeneralException;

public class GoalException extends GeneralException {

    public GoalException(ErrorCode errorCode) {
        super(errorCode);
    }
}
