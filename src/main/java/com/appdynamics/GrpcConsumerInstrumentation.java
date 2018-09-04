package com.appdynamics;

import com.appdynamics.instrumentation.sdk.Rule;
import com.appdynamics.instrumentation.sdk.SDKClassMatchType;
import com.appdynamics.instrumentation.sdk.SDKStringMatchType;
import com.appdynamics.instrumentation.sdk.contexts.ISDKUserContext;
import com.appdynamics.instrumentation.sdk.template.AEntry;
import com.appdynamics.instrumentation.sdk.toolbox.reflection.ReflectorException;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class GrpcConsumerInstrumentation extends AEntry {

    private static final String CLASS_TO_INSTRUMENT = "io.grpc.ServerCallHandler";
    private static final String METHOD_TO_INSTRUMENT = "startCall";
    private boolean identifyBt = true;

    public GrpcConsumerInstrumentation() {
        super();
    }

    @Override
    public List<Rule> initializeRules() {
        List<Rule> result = new ArrayList<>();

        Rule.Builder bldr = new Rule.Builder(CLASS_TO_INSTRUMENT);
        bldr = bldr.classMatchType(SDKClassMatchType.IMPLEMENTS_INTERFACE).classStringMatchType(SDKStringMatchType.EQUALS);
        bldr = bldr.methodMatchString(METHOD_TO_INSTRUMENT).methodStringMatchType(SDKStringMatchType.EQUALS);
        result.add(bldr.build());
        return result;
    }

    @Override
    public String unmarshalTransactionContext(Object invokedObject, String className, String methodName,
                                              Object[] paramValues, ISDKUserContext context)  throws ReflectorException {
        String result = null;
        try {
            if (paramValues != null && paramValues.length > 0) {
                Object o = paramValues[1];
                if (o != null){
                    MetadataKeyUtility metadataKeyUtility = new MetadataKeyUtility();
                    Object CUSTOM_HEADER_KEY = metadataKeyUtility.createMetadataKey(getNewReflectionBuilder(),o,getLogger());
                    Class<?> METADATA_KEY_CLASS = o.getClass().getClassLoader().loadClass("io.grpc.Metadata$Key");
                    Method method = o.getClass().getMethod("get", METADATA_KEY_CLASS );
                    String transactionContext = (String)method.invoke(o, CUSTOM_HEADER_KEY);
                    return transactionContext;
                }
            }

        } catch (Exception et) {
        }
        return result;
    }

    @Override
    public String getBusinessTransactionName(Object invokedObject, String className,
                                             String methodName, Object[] paramValues, ISDKUserContext context)  throws ReflectorException{
        String result = null;
        if (identifyBt)
            result = new String("gRPC Receive");
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
}