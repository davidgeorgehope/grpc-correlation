package com.appdynamics;


import com.appdynamics.apm.appagent.api.ITransactionDemarcator;
import com.appdynamics.instrumentation.sdk.Rule;
import com.appdynamics.instrumentation.sdk.SDKClassMatchType;
import com.appdynamics.instrumentation.sdk.SDKStringMatchType;
import com.appdynamics.instrumentation.sdk.contexts.ISDKUserContext;
import com.appdynamics.instrumentation.sdk.template.AExit;
import com.appdynamics.instrumentation.sdk.toolbox.reflection.IReflector;
import com.appdynamics.instrumentation.sdk.toolbox.reflection.ReflectorException;
import com.appdynamics.instrumentation.sdk.toolbox.reflection.ReflectorFactory;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class GrpcProducerInstrumentation extends AExit {

    private static final String CLASS_TO_INSTRUMENT = "io.grpc.ClientCall";
    private static final String METHOD_TO_INSTRUMENT = "start";

    Collection<UUID> li = new ArrayList<UUID>(10);

    private boolean haveCorrelation = false;

    public GrpcProducerInstrumentation() {
        super();
        boolean searchSuperClass = true;
    }

    @Override
    public List<Rule> initializeRules() {
        List<Rule> result = new ArrayList<>();
        Rule.Builder bldr = new Rule.Builder(CLASS_TO_INSTRUMENT);
        bldr = bldr.classMatchType(SDKClassMatchType.INHERITS_FROM_CLASS).classStringMatchType(SDKStringMatchType.EQUALS);
        bldr = bldr.methodMatchString(METHOD_TO_INSTRUMENT).methodStringMatchType(SDKStringMatchType.EQUALS);
        result.add(bldr.build());
        return result;
    }

    @Override
    public boolean isCorrelationEnabled() {
        return true;
    }

    @Override
    public boolean isCorrelationEnabledForOnMethodBegin() {
        return true;
    }

    @Override
    public void marshalTransactionContext(String transactionContext, Object invokedObject, String className, String methodName, Object[] paramValues, Throwable thrownException, Object returnValue, ISDKUserContext context) throws ReflectorException {
        if (haveCorrelation)
            return;
        if (paramValues != null && paramValues.length > 0) {
            Object o = paramValues[1];
            if (o == null) {

            } else {

                MetadataKeyUtility metadataKeyUtility = new MetadataKeyUtility();
                Object CUSTOM_HEADER_KEY = metadataKeyUtility.getMetaDataKey(getNewReflectionBuilder(),o,getLogger());

                try {

                    Class<?> METADATA_KEY_CLASS = o.getClass().getClassLoader().loadClass("io.grpc.Metadata$Key");

                    Method method = o.getClass().getMethod("put", METADATA_KEY_CLASS, Object.class );
                    method.invoke(o, CUSTOM_HEADER_KEY, transactionContext);


                } catch (Exception e) {
                    getLogger().info("Cannot add header ", e);
                }

            }
        } else {

        }

    }

    @Override
    public Map<String, String> identifyBackend(Object invokedObject, String className, String methodName, Object[] paramValues, Throwable thrownException, Object returnValue, ISDKUserContext context) throws ReflectorException {

        Map<String, String> map = new HashMap<String, String>();
        Object o = paramValues[0];
        String topicStr = "gRPCTest";
        map.put("gRPC", topicStr);
        return map;
    }

    @Override
    public boolean resolveToNode() {
        return true;
    }

    @Override
    public boolean identifyOnEnd() {
        return false;
    }
}
