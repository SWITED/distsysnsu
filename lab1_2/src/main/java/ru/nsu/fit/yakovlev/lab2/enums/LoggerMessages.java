package ru.nsu.fit.yakovlev.lab2.enums;

import lombok.Getter;

public enum LoggerMessages {
    TAG_PARSING_PROCESS("Parsing tags: {0}"),
    PARSING_STARTED("Parsing started"),
    PARSING_FINISHED("Parsing finished"),
    TIMER_MESSAGE("Time: {0} seconds");

    @Getter
    private final String message;

    LoggerMessages(String s) {
        message = s;
    }
}
