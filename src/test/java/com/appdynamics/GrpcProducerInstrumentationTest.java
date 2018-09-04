package com.appdynamics;

import com.appdynamics.apm.appagent.api.ITransactionDemarcator;
import com.appdynamics.instrumentation.sdk.logging.ISDKLogger;
import com.appdynamics.instrumentation.sdk.toolbox.reflection.ReflectorFactory;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GrpcProducerInstrumentationTest {
    private ISDKLogger logger;

    private final String EXPECTED_RESULT = "TEST";
    private final String singularityHeader = ITransactionDemarcator.APPDYNAMICS_TRANSACTION_CORRELATION_HEADER;


    @Test
    public void shouldAddSingularityHeaderToMetadata() throws Exception{
        System.setProperty("javaagent.reflector.factory.impl", "com.singularity.ee.agent.appagent.kernel.reflection.reflector.c");

        //GIVEN: A NEW/EMPTY METADATA OBJECT

        ArrayList<Object> paramValues = new ArrayList<Object>();
        io.grpc.Metadata metaData = new io.grpc.Metadata();
        paramValues.add("");
        paramValues.add(metaData);

        //WHEN: THE NEW/EMPTY METADATA OBJECT AND HEADER "TEST" IS PASSED INTO THE MARSHAL TRANSACTION CONTEXT METHOD

        GrpcProducerInstrumentation grpcProducerInstrumentation = new GrpcProducerInstrumentation();
        grpcProducerInstrumentation.marshalTransactionContext(EXPECTED_RESULT,null,null,null,paramValues.toArray(),null,null,null);

        //THEN: THE METADATA OBJECT CONTAINS THE HEADER

        io.grpc.Metadata.Key<String> CUSTOM_HEADER_KEY =
                io.grpc.Metadata.Key.of(singularityHeader, io.grpc.Metadata.ASCII_STRING_MARSHALLER);

        String result = (String)metaData.get(CUSTOM_HEADER_KEY);

        assertEquals(result,EXPECTED_RESULT);

    }



}
