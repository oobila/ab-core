package com.github.oobila.bukkit.command;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class Argument<T> {

    @Getter
    private String name;

    @Getter
    private Class<T> type;

    @Getter @Setter
    private List<T> fixedSuggestions;

    @Getter @Setter
    private SuggestionCallable<T> suggestionCallable;

    @Getter @Setter
    private T min;

    @Getter @Setter
    private T max;

    Argument(String name, Class<T> type) {
        this.name = name;
        this.type = type;
    }
}
