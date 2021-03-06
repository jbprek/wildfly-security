ejb-security:  Using Java EE Declarative Security to Control Access to EJB 3
====================
Author: Sherif F. Makary
Level: Intermediate
Technologies: EJB, Security
Summary: Shows how to use Java EE Declarative Security to Control Access to EJB 3
Target Project: WildFly
Source: <https://github.com/wildfly/quickstart/>

What is it?
-----------

This example demonstrates the use of Java EE declarative security to control access to Servlets and EJBs in *WildFly*.

This quickstart takes the following steps to implement EJB security:

1. Define the security domain. This can be done either in the `security` subsytem of the `standalone.xml` configuration file or in the `WEB-INF/jboss-web.xml` configuration file. This quickstart uses the `other` security domain which is provided by default in the `standalone.xml` file:

        <security-domain name="other" cache-type="default">
            <authentication>
                <login-module code="Remoting" flag="optional">
                    <module-option name="password-stacking" value="useFirstPass"/>
                </login-module>
                <login-module code="RealmDirect" flag="required">
                    <module-option name="password-stacking" value="useFirstPass"/>
                </login-module>
            </authentication>
        </security-domain>

2. Add the `@SecurityDomain("other")` security annotation to the EJB declaration to tell the EJB container to apply authorization to this EJB.
3. Add the `@RolesAllowed({ "guest" })` annotation to the EJB declaration to authorize access only to users with `guest` role access rights.
4. Add the `@RolesAllowed({ "guest" })` annotation to the Servlet declaration to authorize access only to users with `guest` role access rights.
5. Add a `<login-config>` security constraint to the `WEB-INF/web.xml` file to force the login prompt.
6. Add an application user with `guest` role access rights to the EJB. This quickstart defines a user `quickstartUser` with password `quickstartPwd1!` in the `guest` role. The `guest` role matches the allowed user role defined in the `@RolesAllowed` annotation in the EJB.
7. Add a second user that has no `guest` role access rights.


System requirements
-------------------

All you need to build this project is Java 7.0 (Java SDK 1.7) or better, Maven 3.1 or better.

The application this project produces is designed to be run on JBoss WildFly.


Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](../README.md#mavenconfiguration) before testing the quickstarts.


Add the Application Users
---------------

This quickstart uses a secured management interface and requires that you create an application user to access the running application. Instructions to set up an Application user can be found here:  [Add an Application User](../README.md#addapplicationuser)

For this quickstart it is recommended you create a user called `quickstartUser` with a password of `quickstartPwd1!` and ensure they are granted the role `guest`.

After you add the default `quickstartUser`, use the same steps to add a second application user who is not in the `guest` role and therefore is not authorized to access the application.

        Username: user1
        Password: password1!
        Roles:    app-user


Start JBoss WildFly with the Web Profile
-------------------------

1. Open a command line and navigate to the root of the JBoss server directory.
2. The following shows the command line to start the server with the web profile:

        For Linux:   JBOSS_HOME/bin/standalone.sh
        For Windows: JBOSS_HOME\bin\standalone.bat


Build and Deploy the Quickstart
-------------------------

_NOTE: The following build command assumes you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line. See [Build and Deploy the Quickstarts](../README.md#buildanddeploy) for complete instructions and additional options._

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive:

        mvn clean package wildfly:deploy

4. This will deploy `target/wildfly-ejb-security.war` to the running instance of the server.


Access the application
---------------------

The application will be running at the following URL <http://localhost:8080/my-ejb-security/>.

When you access the application, you are presented with a browser login challenge.

1. If you attempt to login with a user name and password combination that has not been added to the server, the login challenge will be redisplayed.
2. When you login successfully using `quickstartUser`/`quickstartPwd1!`, the browser displays the following security info:

        Successfully called Secured EJB

        Principal : quickstartUser
        Remote User : quickstartUser
        Authentication Type : BASIC

3. Now close and reopen the brower session and access the application using the `user1`/`password1!` credentials. In this case, the Servlet, which only allows the `guest` role, restricts the access and you get a security exception similar to the following:

        HTTP Status 403 - Access to the requested resource has been denied

        type Status report
        message Access to the requested resource has been denied
        description Access to the specified resource (Access to the requested resource has been denied) has been forbidden.

4. Next, change the EJB (SecuredEJB.java) to a different role, for example, `@RolesAllowed({ "other-role" })`. Do not modify the `guest` role in the Servlet (SecuredEJBServlet.java). Build and redeploy the quickstart, then close and reopen the browser and login using `quickstartUser`/`quickstartPwd1!`. This time the Servlet will allow the `guest` access, but the EJB, which only allows the role `other-role`, will throw an EJBAccessException:

        HTTP Status 500

        message
        description  The server encountered an internal error () that prevented it from fulfilling this request.
        exception
        javax.ejb.EJBAccessException: JBAS014502: Invocation on method: public java.lang.String SecuredEJB.getSecurityInfo() of bean: SecuredEJB is not allowed

Remote Client
-------------

In addition to access through the web application it is also possible to access the secured EJB from a remote stand alone client.

1.  Type this command to execute the client:

		mvn exec:exec

This will execute the remote client which connects to the server and invokes the `getSecurityInfo()` method of the EJB to obtain the name of the current authenticated user.

One important file to note is under `src/main/resources` and is called `jboss-ejb-client.properties` this file contains the settings to connect to the server and also supplies the username and password of the user to connect as.

Undeploy the Archive
--------------------

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn wildfly:undeploy


Run the Quickstart in JBoss Developer Studio or Eclipse
-------------------------------------
You can also start the server and deploy the quickstarts from Eclipse using JBoss tools. For more information, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](../README.md#useeclipse)


Debug the Application
------------------------------------

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following commands to pull them into your local repository. The IDE should then detect them.

    mvn dependency:sources
    mvn dependency:resolve -Dclassifier=javadoc
