package org.zerotul.specification.recorder;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Created by zerotul on 12.03.15.
 */
public class EnhancerRecorder<T extends Serializable> implements Recorder<T> {

    private static final long serialVersionUID = 5781727045810215034L;

    private final T  object;

    private final Class<T> clazz;

    private ThreadLocal<String> currentPropertyName;

    private ThreadLocal<Class> currentPropertyType;

    private EnhancerRecorder(T object, Class<T> clazz) {
        this.object = object;
        this.clazz = clazz;
        this.currentPropertyName = new ThreadLocal<>();
        this.currentPropertyType = new ThreadLocal<>();
    }


    public static <T extends Serializable> EnhancerRecorder<T> create(Class<T> clazz){
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clazz);
        Interceptor<T> interceptor = new Interceptor<>(clazz);
        enhancer.setCallback(interceptor);
        EnhancerRecorder<T> recorder = new EnhancerRecorder<>((T) enhancer.create(), clazz);
        interceptor.setRecorder(recorder);
        return recorder;
    }

    @Override
    public  <R> String getPropertyName(Function<T, R> getter){
        try{
            getter.apply(object);
            return currentPropertyName.get();
        }finally {
            this.currentPropertyName.set(null);
            this.currentPropertyType.set(null);
        }
    }

    @Override
    public synchronized  <R> Class<R> getPropertyType(Function<T, R> getter) {
        try{
            getter.apply(object);
            return currentPropertyType.get();
        }finally {
            this.currentPropertyName.set(null);
            this.currentPropertyType.set(null);
        }
    }


    private static class Interceptor<T extends Serializable>  implements MethodInterceptor {
        private final Map<String, String> propertyMap;

        private EnhancerRecorder<T> recorder;

        private final Class<T> clazz;

        public Interceptor(Class<T> clazz) {
            this.propertyMap = new HashMap<>();
            this.clazz = clazz;
        }

        @Override
        public synchronized Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
            String propertyName = this.propertyMap.get(method.getName());
            if(propertyName!=null) {
                recorder.currentPropertyName.set(propertyName);
                recorder.currentPropertyType.set(method.getReturnType());
                return methodProxy.invokeSuper(o, args);
            }

            BeanInfo beanInfo = Introspector.getBeanInfo(this.clazz);
            PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();
            for(PropertyDescriptor descriptor : descriptors){
                if(descriptor.getReadMethod().getName().equals(method.getName())){
                    propertyName = descriptor.getName();
                    this.propertyMap.put(method.getName(), propertyName);
                    break;
                }
            }
            if (propertyName==null) throw new IllegalStateException("property for a method "+method.getName()+" in a class "+this.clazz+" not found");
            recorder.currentPropertyName.set(propertyName);
            recorder.currentPropertyType.set(method.getReturnType());
            return methodProxy.invokeSuper(o, args);
        }

        protected void setRecorder(EnhancerRecorder<T> recorder){
            this.recorder = recorder;
        }
    }
}
