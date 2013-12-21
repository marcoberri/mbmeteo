/**
 *  Copyright 2011 Marco Berri - marcoberri@gmail.com
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 **/
package it.marcoberri.mbmeteo.utils;

import java.lang.reflect.*;
import org.apache.log4j.*;

/**
 * Utilities to work with reflection.
 *
 * @author Erich Roncarolo
 * @version 0.1 - 2004-09-07
 */
public class ClassUtil {

    private ClassUtil() {
    }//ClassUtil()

    /**
     * Returns a new instance of an object using constructor
     * without arguments.
     *
     * @param name class name
     * @return a new object instance or null if some error occours (exceptions are logged)
     */
    public static Object newInstance(String name) {
        return newInstance(name, null, null, false);
    }//newInstance()

    /**
     * Returns a new instance of an object using constructor
     * with arguments' running classes as parameter types.
     *
     * @param name class name
     * @param args constructor initialization arguments
     *
     * @return a new object instance or null if some error occours (exceptions are logged)
     */
    public static Object newInstance(String name, Object args[]) {
        return newInstance(name, null, args, false);
    }//newInstance()

    /**
     * Returns a new instance of an object using constructor with
     * specified parameter types and arguments.
     *
     * @param name class name
     * @param param constructor parameter types
     * @param args constructor initialization arguments
     *
     * @return a new object instance or null if some error occours (exceptions are logged)
     */
    public static Object newInstance(String name, Class param[], Object args[]) {
        return newInstance(name, param, args, false);
    }//newInstance()

    /**
     * Returns a new instance of an object using constructor with
     * specified parameter types and arguments.
     *
     * @param name class name
     * @param param constructor parameter types
     * @param args constructor initialization arguments
     * @param logException constructor initialization arguments
     *
     * @return a new object instance or null if some error occours (exceptions are logged)
     */
    public static Object newInstance(String name, Class param[], Object args[], boolean logException) {
        if (param == null && args != null) {
            param = new Class[args.length];
            for (int i = 0; i < param.length; i++) {
                if (args[i] != null) {
                    param[i] = args[i].getClass();
                } else {
                    param[i] = null;
                }
            }//for
        }//if

        Constructor c = null;
        Object obj = null;
        try {
            c = Class.forName(name).getConstructor(param);
            if (c != null) {
                obj = c.newInstance(args);
            }
        } catch (Exception x) {
            if (logException) {
                Logger log = Logger.getLogger(ClassUtil.class);
                log.error("", x);
            }
            return null;
        }
        return obj;
    }//newInstance()

    /**
     * Returns a new instance of an object using constructor
     * without arguments.
     *
     * @param clazz object class
     * @return a new object instance or null if some error occours (exceptions are logged)
     */
    public static Object newInstance(Class clazz) {
        return newInstance(clazz, null, null);
    }//newInstance()

    /**
     * Returns a new instance of an object using constructor
     * with arguments' running classes as parameter types.
     *
     * @param clazz object class
     * @param args constructor initialization arguments
     *
     * @return a new object instance or null if some error occours (exceptions are logged)
     */
    public static Object newInstance(Class clazz, Object args[]) {
        return newInstance(clazz, null, args);
    }//newInstance()

    /**
     * Returns a new instance of an object using constructor with
     * specified parameter types and arguments.
     *
     * @param clazz object class
     * @param param constructor parameter types
     * @param args constructor initialization arguments
     *
     * @return a new object instance or null if some error occours (exceptions are logged)
     */
    public static Object newInstance(Class clazz, Class param[], Object args[]) {
        if (clazz == null) {
            return null;
        }
        String name = clazz.getName();
        return newInstance(name, param, args);
    }//newInstance()

    /**
     * Invokes the object's method with specified parameter types and arguments.
     *
     * @param obj the object the specified method is invoked from (use a Class object if method is static)
     * @param name method name
     * @param param constructor parameter types
     * @param args constructor initialization arguments
     *
     * @return a new object instance or null if some error occours (exceptions are logged)
     */
    public static Object invokeMethod(Object obj, String name, Class param[], Object args[]) {
        if (param == null && args != null) {
            param = new Class[args.length];
            for (int i = 0; i < param.length; i++) {
                if (args[i] != null) {
                    param[i] = args[i].getClass();
                } else {
                    param[i] = null;
                }
            }//for
        }//if

        Method m = null;
        Object ret = null;
        try {
            if (obj instanceof Class) {
                m = ((Class) obj).getMethod(name, param);
            } else {
                m = obj.getClass().getMethod(name, param);
            }
            if (m != null) {
                ret = m.invoke(obj, args);
            }
        } catch (NoSuchMethodException x) {
            // Logger log = Logger.getLogger(ClassUtil.class);
            //log.warn("No method "+(obj instanceof Class ? (Class)obj : obj.getClass()).toString()+"."+name+"("+Arrays.asList(param)+") found - "+TnesException.firstStackTrace(x));
            return null;
        } catch (Exception x) {
            Logger log = Logger.getLogger(ClassUtil.class);
            log.error("", x);
            return null;
        }
        return ret;
    }//invokeMethod()

    /**
     * Invokes the object's method with specified arguments.
     *
     * @param obj the object the specified method is invoked from (use a Class object if method is static)
     * @param name method name
     * @param args constructor initialization arguments
     *
     * @return a new object instance or null if some error occours (exceptions are logged)
     */
    public static Object invokeMethod(Object obj, String name, Object args[]) {
        return invokeMethod(obj, name, null, args);
    }//invokeMethod()

    /**
     * Invokes the object's method with no arguments.
     *
     * @param obj the object the specified method is invoked from (use a Class object if method is static)
     * @param name method name
     *
     * @return a new object instance or null if some error occours (exceptions are logged)
     */
    public static Object invokeMethod(Object obj, String name) {
        return invokeMethod(obj, name, null, null);
    }//invokeMethod()
}//ClassUtil
