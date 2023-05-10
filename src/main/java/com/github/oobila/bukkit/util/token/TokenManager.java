package com.github.oobila.bukkit.util.token;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TokenManager {

    private static final Map<Token, Object> tokenMap = new HashMap<>();
    private static final Map<Class, Set<Token>> tokensByClass = new HashMap<>();

    static boolean addToken(Token token, Object object){
        if(tokenMap.containsKey(token)){
            return false;
        }
        tokenMap.put(token, object);
        tokensByClass.computeIfAbsent(token.getObjectType(), t -> new HashSet<>());
        tokensByClass.get(token.getObjectType()).add(token);
        return true;
    }

    public Object getTokenObject(Token token){
        return tokenMap.get(token);
    }

    public Set<Token> getTokens(Class _class){
        return tokensByClass.get(_class);
    }

}
