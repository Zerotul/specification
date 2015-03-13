package org.zerotul.specification;

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
public class Recorder<T extends Serializable> implements Serializable{

    private static final long serialVersionUID = 5781727045810215034L;

    private final T  object;

    private final Class<T> clazz;

    private String currentPropertyName;

    private Recorder(T object, Class<T> clazz) {
        this.object = object;
        this.clazz = clazz;
    }

    public T getObject() {
        return object;
    }

    public static <T extends Serializable> Recorder<T> create(Class<T> clazz){
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clazz);
        Interceptor<T> interceptor = new Interceptor(clazz);
        enhancer.setCallback(interceptor);
        Recorder<T> recorder = new Recorder<>((T) enhancer.create(), clazz);
        interceptor.setRecorder(recorder);
        return recorder;
    }

    public synchronized <R> String getPropertyName(Function<T, R> getter){
        try{
            getter.apply(object);
            String propertyName = currentPropertyName;
            return propertyName;
        }finally {
            this.currentPropertyName = null;
        }
    }


    private static class Interceptor<T extends Serializable>  implements MethodInterceptor {
        private final Map<String, String> propertyMap;

        private Recorder<T> recorder;

        private final Class<T> clazz;

        public Interceptor(Class<T> clazz) {
            this.propertyMap = new HashMap<>();
            this.clazz = clazz;
        }

        @Override
        public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
            String propertyName = this.propertyMap.get(method.getName());
            if(propertyName!=null) {
                recorder.currentPropertyName =propertyName;
                return methodProxy.invokeSuper(o, args);
            };

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
            recorder.currentPropertyName = propertyName;
            return methodProxy.invokeSuper(o, args);
        }

        protected void setRecorder(Recorder<T> recorder){
            this.recorder = recorder;
        }
    }
}
