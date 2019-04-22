/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ir.amv.snippets.my.enhancer;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.framework.hooks.service.EventListenerHook;
import org.osgi.framework.hooks.service.FindHook;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class Activator implements BundleActivator {

    private List<ServiceRegistration<?>> registrations = new ArrayList<>();

    public void start(BundleContext context) {
        System.out.println("Starting the bundle");
//        registrations.add(context.registerService(WeavingHook.class, new CaptainHook(), new Hashtable<>()));
        registrations.add(context.registerService(EventListenerHook.class, new MyEventHook(context), new Hashtable<>()));
        registrations.add(context.registerService(FindHook.class, new MyFindHook(context), new Hashtable<>()));
    }

    public void stop(BundleContext context) {
        System.out.println("Stopping the bundle");
        registrations.forEach(ServiceRegistration::unregister);
    }

}