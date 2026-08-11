package com.github.chenyuxin.commonframework.daojpa.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;

import jakarta.persistence.EntityManager;

public class DaoResourceTest {

    private DaoResource daoResource;
    private ApplicationContext applicationContextStub;
    private EntityManager defaultEntityManagerStub;
    private EntityManager customEntityManagerStub;

    @BeforeEach
    public void setUp() throws Exception {
        daoResource = new DaoResource();

        defaultEntityManagerStub = createStub(EntityManager.class, "defaultEM");
        customEntityManagerStub = createStub(EntityManager.class, "customEM");

        applicationContextStub = (ApplicationContext) Proxy.newProxyInstance(
                ApplicationContext.class.getClassLoader(),
                new Class[] { ApplicationContext.class },
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        if (method.getName().equals("getBean") && args.length > 0) {
                            String beanName = (String) args[0];
                            if ("customDSEntityManager".equals(beanName)) {
                                return customEntityManagerStub;
                            } else if ("missingDSEntityManager".equals(beanName)) {
                                throw new RuntimeException("Bean not found: " + beanName);
                            }
                        }
                        if (method.getName().equals("toString"))
                            return "ApplicationContextStub";
                        if (method.getName().equals("hashCode"))
                            return System.identityHashCode(proxy);
                        if (method.getName().equals("equals") && args.length == 1)
                            return proxy == args[0];

                        throw new UnsupportedOperationException("Not implemented: " + method.getName());
                    }
                });

        // Inject stubs using reflection
        setField(daoResource, "applicationContext", applicationContextStub);
        setField(daoResource, "entityManager", defaultEntityManagerStub);
    }

    @SuppressWarnings("unchecked")
    private <T> T createStub(Class<T> interfaceType, String name) {
        return (T) Proxy.newProxyInstance(
                interfaceType.getClassLoader(),
                new Class[] { interfaceType },
                (proxy, method, args) -> {
                    if (method.getName().equals("toString"))
                        return name;
                    if (method.getName().equals("hashCode"))
                        return name.hashCode();
                    if (method.getName().equals("equals") && args.length == 1) {
                        return proxy == args[0];
                    }
                    return null;
                });
    }

    private void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    @Test
    public void testMoreEntityManagerDefault() {
        assertEquals(defaultEntityManagerStub, daoResource.moreEntityManager(null));
        assertEquals(defaultEntityManagerStub, daoResource.moreEntityManager(""));
        assertEquals(defaultEntityManagerStub, daoResource.moreEntityManager("dataSource"));
    }

    @Test
    public void testMoreEntityManagerCustom() {
        EntityManager result = daoResource.moreEntityManager("customDS");
        assertEquals(customEntityManagerStub, result);
    }

    @Test
    public void testMoreEntityManagerNotFound() {
        assertThrows(RuntimeException.class, () -> {
            daoResource.moreEntityManager("missingDS");
        });
    }
}
