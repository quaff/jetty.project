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

package org.eclipse.jetty.websocket.common;

import java.lang.invoke.MethodHandle;
import java.util.concurrent.Executor;

import org.eclipse.jetty.websocket.core.WebSocketPolicy;

public abstract class MessageSinkImpl implements MessageSink
{
    protected final WebSocketPolicy policy;
    protected final Executor executor;
    protected final MethodHandle methodHandle;

    public MessageSinkImpl(WebSocketPolicy policy, Executor executor, MethodHandle methodHandle)
    {
        this.policy = policy;
        this.executor = executor;
        this.methodHandle = methodHandle;
    }
}