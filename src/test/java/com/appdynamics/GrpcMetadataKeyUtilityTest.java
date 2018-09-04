package com.appdynamics;

import com.appdynamics.apm.appagent.api.ITransactionDemarcator;
import com.appdynamics.instrumentation.sdk.logging.ISDKLogger;
import com.appdynamics.instrumentation.sdk.toolbox.reflection.ReflectorFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GrpcMetadataKeyUtilityTest {
    private ISDKLogger logger;


    @Test
    public void createAgRPCMetaDataKeyForInsertionAndRetrieval() throws Exception{
        System.setProperty("javaagent.reflector.factory.impl", "com.singularity.ee.agent.appagent.kernel.reflection.reflector.c");

        //GIVEN: A NEW/EMPTY METADATA OBJECT

        Object metadata = new io.grpc.Metadata();

        //WHEN: A KEY IS CREATED WITH THE CREATE META DATA KEY UTILITY
        MetadataKeyUtility metadataKeyUtility = new MetadataKeyUtility();
        io.grpc.Metadata.Key CUSTOM_HEADER_KEY =  (io.grpc.Metadata.Key)metadataKeyUtility.createMetadataKey(ReflectorFactory.getInstance().getNewReflectionBuilder(),metadata,logger);

        //THEN: THE RETURNED KEY CONTAINS THE HEADER.
        assertTrue(CUSTOM_HEADER_KEY.toString().contains(ITransactionDemarcator.APPDYNAMICS_TRANSACTION_CORRELATION_HEADER));

    }



}
