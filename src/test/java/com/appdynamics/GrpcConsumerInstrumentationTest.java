package com.appdynamics;

import com.appdynamics.apm.appagent.api.ITransactionDemarcator;
import com.appdynamics.instrumentation.sdk.logging.ISDKLogger;
import com.appdynamics.instrumentation.sdk.toolbox.reflection.ReflectorFactory;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GrpcConsumerInstrumentationTest {
    private ISDKLogger logger;

    private final String EXPECTED_RESULT = "TEST";
    private final String singularityHeader = ITransactionDemarcator.APPDYNAMICS_TRANSACTION_CORRELATION_HEADER;

    @Test
    public void shouldReadSingularityHeaderFromMetadata() throws Exception{
        System.setProperty("javaagent.reflector.factory.impl", "com.singularity.ee.agent.appagent.kernel.reflection.reflector.c");

        //GIVEN: A METADATA OBJECT WITH A SINGULARITY HEADER "TEST"
        io.grpc.Metadata metaData = new io.grpc.Metadata();
        io.grpc.Metadata.Key<String> CUSTOM_HEADER_KEY =
                io.grpc.Metadata.Key.of(singularityHeader, io.grpc.Metadata.ASCII_STRING_MARSHALLER);

        metaData.put(CUSTOM_HEADER_KEY,EXPECTED_RESULT);


        //WHEN THAT METADATA OBJECT IS PASSED INTO THE UNMARSHALL TRANSACTION CONTEXT METHOD
        ArrayList<Object> paramValues = new ArrayList<Object>();
        paramValues.add("");
        paramValues.add(metaData);


        GrpcConsumerInstrumentation grpcConsumerInstrumentation = new GrpcConsumerInstrumentation();
        String result = grpcConsumerInstrumentation.unmarshalTransactionContext(null,null,null,paramValues.toArray(),null);

        //THEN IT SHOULD RETURN THE EXPECTED "TEST" HEADER
        assertEquals(result,EXPECTED_RESULT);

    }



}
