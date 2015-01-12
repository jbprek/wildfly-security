/*
 * JBoss, Home of Professional Open Source
 * Copyright 2014, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.bagab.wildfly.security.my_ejb_security;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;

/**
 * The remote client responsible for calling the secured EJB.
 *
 * @author <a href="mailto:darran.lofthouse@jboss.com">Darran Lofthouse</a>
 */
public class RemoteClient {

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        System.out.println("\n\n\n* * * * * * * * * * * * * * * * * * * * * * * * * * * * * *\n\n");

        SecuredRemote remote = lookupEJB();

        String principal = remote.getSecurityInfo();

        System.out.println(String.format("Called EJB, authenticated Principal is '%s'", principal));

        System.out.println("\n\n* * * * * * * * * * * * * * * * * * * * * * * * * * * * * *\n\n\n");
    }

    static SecuredRemote lookupEJB() throws Exception {
        final Hashtable<String, String> jndiProperties = new Hashtable<String, String>();
        jndiProperties.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
        final Context context = new InitialContext(jndiProperties);

        return (SecuredRemote) context.lookup("ejb:/my-ejb-security/SecuredEJB!" + SecuredRemote.class.getName());
    }

}
