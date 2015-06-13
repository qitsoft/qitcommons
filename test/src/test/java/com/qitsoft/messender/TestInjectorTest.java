package com.qitsoft.messender;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;

public class TestInjectorTest {

    @Test
    public void testInjectByType() throws IllegalAccessException {
        SampleService1 service = new SampleService1();
        Component1 component = new Component1();

        TestInjector.inject(service, component);

        assertThat(service.componentOne, sameInstance(component));
    }

    @Test
    public void testInjectOnlyFirst() throws IllegalAccessException {
        SampleService1 service = new SampleService1();
        Component1 component = new Component1();

        TestInjector.inject(service, component);

        assertThat(service.componentOne, sameInstance(component));
        assertThat(service.componentOne1, nullValue());
    }

    @Test
    public void testInjectDescendant() throws IllegalAccessException {
        SampleService1 service = new SampleService1();
        Component1 component = new Component11();

        TestInjector.inject(service, component);

        assertThat(service.componentOne, sameInstance(component));
    }

    @Test
    public void testInjectInChildFields() throws IllegalAccessException {
        SampleService1 service = new SampleService11();
        Component1 component = new Component11();

        TestInjector.inject(service, component);

        assertThat(service.componentOne, sameInstance(component));
    }

    @Test
    public void testInjectMissingField() throws IllegalAccessException {
        SampleService1 service = new SampleService11();

        TestInjector.inject(service, "hello");
    }

    @Test
    public void testInjectGeneric() throws IllegalAccessException {
        SampleService2 service = new SampleService2();
        List<Component1> components = Arrays.asList(new Component1());

        TestInjector.inject(service, components, Component1.class);

        assertThat(service.components, sameInstance(components));
    }

    private static class SampleService11 extends SampleService1{

    }

    private static class SampleService1 {

        private Component1 componentOne;

        private Component1 componentOne1;

    }

    private static class SampleService2 {

        private List<String> springs;

        private List<Component1> components;

    }

    private static class Component1 {}

    private static class Component11 extends Component1 {}

}