package com.appdynamics;

import com.appdynamics.apm.appagent.api.ITransactionDemarcator;
import com.appdynamics.instrumentation.sdk.logging.ISDKLogger;
import com.appdynamics.instrumentation.sdk.toolbox.reflection.IReflectionBuilder;
import com.appdynamics.instrumentation.sdk.toolbox.reflection.IReflector;

import java.lang.reflect.Method;
import java.util.logging.Logger;

public class MetadataKeyUtility {

    private static final String META_DATA_KEY = "io.grpc.Metadata$Key";

    public Object getMetaDataKey(IReflectionBuilder reflectionBuilder, Object o, ISDKLogger logger){

        //Get the custom header key
        try {

            IReflector preASCII_STRING_MARSHALLER = reflectionBuilder
                    .accessFieldValue("ASCII_STRING_MARSHALLER", true).build();

            Object ASCII_STRING_MARSHALLER = preASCII_STRING_MARSHALLER.execute(o.getClass().getClassLoader(), o);

            Class<?> innerClass = o.getClass().getClassLoader().loadClass(META_DATA_KEY);
            Class<?> ASCII_STRING_MARSHALLER_CLASS = o.getClass().getClassLoader().loadClass("io.grpc.Metadata$AsciiMarshaller");

            logger.info("Fields" + innerClass);

            Method method = innerClass.getMethod("of", String.class, ASCII_STRING_MARSHALLER_CLASS);
            Object o2 = method.invoke(null, ITransactionDemarcator.APPDYNAMICS_TRANSACTION_CORRELATION_HEADER, ASCII_STRING_MARSHALLER);

            return o2;

        }
        catch (Exception e) {
            logger.info("Cannot get custom header key", e);
        }
        return null;
    }
}
