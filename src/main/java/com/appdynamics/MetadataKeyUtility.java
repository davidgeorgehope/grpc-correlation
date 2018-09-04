package com.appdynamics;

import com.appdynamics.apm.appagent.api.ITransactionDemarcator;
import com.appdynamics.instrumentation.sdk.logging.ISDKLogger;
import com.appdynamics.instrumentation.sdk.toolbox.reflection.IReflectionBuilder;
import com.appdynamics.instrumentation.sdk.toolbox.reflection.IReflector;

import java.lang.reflect.Method;

public class MetadataKeyUtility {

    private static final String META_DATA_KEY = "io.grpc.Metadata$Key";

    public Object createMetadataKey(IReflectionBuilder reflectionBuilder, Object o, ISDKLogger logger){

        /*This utility method creates a GRPC Key field.

        We need this for both the producer and consumer, for the producer we create the key based on the
        APPDYNAMICS_TRANSACTION_CORRELATION_HEADER and use it in a k/v pattern to insert it into the header.

        For the consumer we create the key and then lookup the header using the key we created.

        This method uses reflection to perform the equivalent following action:

                io.grpc.Metadata.Key<String> CUSTOM_HEADER_KEY =
                io.grpc.Metadata.Key.of(singularityHeader, io.grpc.Metadata.ASCII_STRING_MARSHALLER);

        */

        try {

            //io.grpc.Metadata.ASCII_STRING_MARSHALLER
            Object metadataASCII_STRING_MARSHALLERField = getGgrpcMetadataASCII_STRING_MARSHALLERField(reflectionBuilder, o);

            //io.grpc.Metadata.Key.of
            Method metadataKeyofMethod = getMetadataKeyOfMethod(o);

            //invoke the io.grpc.Metadata.Key.of method with the ASCII_STRING_MARSHALLER field value to create the key.
            Object metaDataKey = metadataKeyofMethod.invoke(null, ITransactionDemarcator.APPDYNAMICS_TRANSACTION_CORRELATION_HEADER, metadataASCII_STRING_MARSHALLERField);

            return metaDataKey;

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Method getMetadataKeyOfMethod(Object o) throws ClassNotFoundException, NoSuchMethodException {
        //find the io.grpc.Metadata.Key.of method....
        Class<?> innerClass = o.getClass().getClassLoader().loadClass(META_DATA_KEY);
        Class<?> ASCII_STRING_MARSHALLER_CLASS = o.getClass().getClassLoader().loadClass("io.grpc.Metadata$AsciiMarshaller");
        return innerClass.getMethod("of", String.class, ASCII_STRING_MARSHALLER_CLASS);
    }

    private Object getGgrpcMetadataASCII_STRING_MARSHALLERField(IReflectionBuilder reflectionBuilder, Object o) throws com.appdynamics.instrumentation.sdk.toolbox.reflection.ReflectorException {
        //Get the field value io.grpc.Metadata.ASCII_STRING_MARSHALLER):
        IReflector ASCII_STRING_MARSHALLER = reflectionBuilder
                .accessFieldValue("ASCII_STRING_MARSHALLER", true).build();

        return ASCII_STRING_MARSHALLER.execute(o.getClass().getClassLoader(), o);
    }
}
