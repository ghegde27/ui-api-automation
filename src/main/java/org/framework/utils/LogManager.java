package org.framework.utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public interface LogManager {
         static Logger getLogger(String name){
            Objects.requireNonNull(name);
            return LoggerFactory.getLogger( name );
    }

     static Logger getLogger(Class clz) {
         Objects.requireNonNull(clz);
         return LoggerFactory.getLogger( clz );

     }








}
