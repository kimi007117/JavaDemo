package com.android.core.arouter;

import android.text.TextUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * Created by lijie on 2019-06-27.
 */
final class ServiceMethod<T> {


    ServiceMethod(Builder<T> builder) {

    }

    static final class Builder<T> {
        String relativeUrl;
        String routeMethod;
        final Router router;
        final Method method;
        final Annotation[] methodAnnotations;
        final Annotation[][] parameterAnnotationsArray;
        final Type[] parameterTypes;

        public Builder(Router router, Method method) {
            this.router = router;
            this.method = method;
            this.methodAnnotations = method.getAnnotations();
            this.parameterTypes = method.getGenericParameterTypes();
            this.parameterAnnotationsArray = method.getParameterAnnotations();
        }

        public ServiceMethod build() {

            for (Annotation annotation : methodAnnotations) {
                parseMethodAnnotation(annotation);
            }

            if (routeMethod == null) {
                throw new IllegalArgumentException("method annotation is required (e.g., @Route, etc.).");
            }

            return new ServiceMethod<>(this);
        }

        private void parseMethodAnnotation(Annotation annotation) {
            if (annotation instanceof Route) {
                parseMethodAndPath("Route", ((Route) annotation).value());
            }
        }

        private void parseMethodAndPath(String method, String value) {
            if (routeMethod != null) {
                throw new IllegalArgumentException("Only one method is allowed");
            }
            this.routeMethod = method;
            if (TextUtils.isEmpty(value)) {
                return;
            }
            this.relativeUrl = value;
        }
    }


}
