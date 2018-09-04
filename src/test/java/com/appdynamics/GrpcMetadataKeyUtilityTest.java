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

        Object metadata = new io.grpc.Metadata();

        MetadataKeyUtility metadataKeyUtility = new MetadataKeyUtility();
        io.grpc.Metadata.Key CUSTOM_HEADER_KEY =  (io.grpc.Metadata.Key)metadataKeyUtility.createMetadataKey(ReflectorFactory.getInstance().getNewReflectionBuilder(),metadata,logger);

        assertTrue(CUSTOM_HEADER_KEY.toString().contains(ITransactionDemarcator.APPDYNAMICS_TRANSACTION_CORRELATION_HEADER));

    }



}
