//
//  ========================================================================
//  Copyright (c) 1995-2018 Mort Bay Consulting Pty. Ltd.
//  ------------------------------------------------------------------------
//  All rights reserved. This program and the accompanying materials
//  are made available under the terms of the Eclipse Public License v1.0
//  and Apache License v2.0 which accompanies this distribution.
//
//      The Eclipse Public License is available at
//      http://www.eclipse.org/legal/epl-v10.html
//
//      The Apache License v2.0 is available at
//      http://www.opensource.org/licenses/apache2.0.php
//
//  You may elect to redistribute this code under either of these licenses.
//  ========================================================================
//

package org.eclipse.jetty.websocket.jsr356.util;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.websocket.Session;

import org.eclipse.jetty.util.annotation.Name;
import org.eclipse.jetty.websocket.jsr356.JavaxWebSocketFrameHandlerFactory;
import org.eclipse.jetty.websocket.jsr356.util.InvokerUtils.Arg;
import org.junit.Test;

public class InvokerUtils_StaticParamsTest
{
    @SuppressWarnings("unused")
    public static class Foo
    {
        public String onFruit(@Name("fruit") String fruit)
        {
            return String.format("onFruit('%s')", fruit);
        }

        public String onCount(@Name("count") int count)
        {
            return String.format("onCount(%d)", count);
        }

        public String onLabeledCount(String label, @Name("count") int count)
        {
            return String.format("onLabeledCount('%s', %d)", label, count);
        }

        public String onColorMessage(Session session, String message, @Name("color") String color)
        {
            return String.format("onColorMessage(%s, '%s', '%s')", color);
        }
    }

    @Test
    public void testOnlyParam_String() throws Throwable
    {
        Method method = ReflectUtils.findMethod(Foo.class, "onFruit", String.class);

        // Declared Variable Names
        final String namedVariables[] = new String[]{
                "fruit"
        };

        // Raw Calling Args - none specified

        // Get basic method handle (without a instance to call against) - this is what the metadata stores
        MethodHandle methodHandle = InvokerUtils.mutatedInvoker(Foo.class, method, new NameParamIdentifier(), namedVariables);

        // Some point later an actual instance is needed, which has static named parameters
        Map<String, String> templateValues = new HashMap<>();
        templateValues.put("fruit", "pear");

        // Bind the static values, in same order as declared
        methodHandle = JavaxWebSocketFrameHandlerFactory.bindTemplateVariables(methodHandle, namedVariables, templateValues);

        // Assign an instance to call.
        Foo foo = new Foo();
        methodHandle = methodHandle.bindTo(foo);

        // Call method against instance
        String result = (String) methodHandle.invoke();
        assertThat("Result", result, is("onFruit('pear')"));
    }

    @Test
    public void testOnlyParam_Int() throws Throwable
    {
        Method method = ReflectUtils.findMethod(Foo.class, "onCount", int.class);

        // Declared Variable Names - as seen in url-template-pattern
        final String namedVariables[] = new String[]{
                "count"
        };

        // Get basic method handle (without a instance to call against) - this is what the metadata stores
        MethodHandle methodHandle = InvokerUtils.mutatedInvoker(Foo.class, method, new NameParamIdentifier(), namedVariables);

        // Some point later an actual instance is needed, which has static named parameters
        Map<String, String> templateValues = new HashMap<>();
        templateValues.put("count", "2222");

        // Bind the static values for the variables, in same order as the variables were declared
        methodHandle = JavaxWebSocketFrameHandlerFactory.bindTemplateVariables(methodHandle, namedVariables, templateValues);

        // Assign an instance to call.
        Foo foo = new Foo();
        methodHandle = methodHandle.bindTo(foo);

        // Call method against instance
        String result = (String) methodHandle.invoke();
        assertThat("Result", result, is("onCount(2222)"));
    }

    @Test
    public void testLabeledParam_StringInt() throws Throwable
    {
        Method method = ReflectUtils.findMethod(Foo.class, "onLabeledCount", String.class, int.class);

        // Declared Variable Names - as seen in url-template-pattern
        final String namedVariables[] = new String[]{
                "count"
        };

        final Arg ARG_LABEL = new Arg(String.class).required();

        // Get basic method handle (without a instance to call against) - this is what the metadata stores
        MethodHandle methodHandle = InvokerUtils.mutatedInvoker(Foo.class, method, new NameParamIdentifier(), namedVariables, ARG_LABEL);

        // Some point later an actual instance is needed, which has static named parameters
        Map<String, String> templateValues = new HashMap<>();
        templateValues.put("count", "444");

        // Bind the static values for the variables, in same order as the variables were declared
        methodHandle = JavaxWebSocketFrameHandlerFactory.bindTemplateVariables(methodHandle, namedVariables, templateValues);

        // Assign an instance to call.
        Foo foo = new Foo();
        methodHandle = methodHandle.bindTo(foo);

        // Call method against instance
        String result = (String) methodHandle.invoke("cherry");
        assertThat("Result", result, is("onLabeledCount('cherry', 444)"));
    }
}
