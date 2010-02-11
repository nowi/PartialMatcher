package de.unima.semweb.partialmatcher.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: nowi
 * Date: 11.02.2010
 * Time: 15:59:32
 */
/*
 * Original credit to Mark Menard:
 * http://www.vitarara.org/cms/whats_wrong_with_javas_dynamic_dispatch_or_how_i_implemented_sendMessage
 * Modified by cdfh to call the most specific method
 */
public class DynDispatch {

    /**
     * Dynamically dispatch message to target.
     * Dispatches to the most specific target.
     */
    public static Object sendMessage(String message, Object target, Object... args) throws Throwable {
        try {
            // Is this an argumentless method call?
            if (args == null) {
                // Get the method.
                return target.getClass().getMethod(message).invoke(target);
            } else {

                // Get all methods from the target.
                Method[] allMethods = target.getClass().getMethods();
                List<Method> callables = new LinkedList();

                NEXT:
                for (int i = 0; i < allMethods.length; i++) {
                    // Filter methods by name and length of arguments.
                    Method m = allMethods[i];
                    if (m.getName().equals(message) && m.getParameterTypes().length == args.length) {

                        for (int j = 0; j < m.getParameterTypes().length; j++) {
                            if (!m.getParameterTypes()[j].isAssignableFrom(args[j].getClass())) {
                                continue NEXT;
                            }
                        }

                        callables.add(m);
                    }
                }

                Collections.sort(callables, new Comparator<Method>() {
                    public int compare(Method a, Method b) {
                        return methodMoreSpecific(a, b);
                    }
                });

                if (callables.isEmpty()) {
                    throw new RuntimeException("No method found for: " + message);
                } else {
                    return callables.iterator().next().invoke(target, args);
                }
            }
        } catch (IllegalAccessException ex) {
            throw new RuntimeException(ex);
        } catch (IllegalArgumentException ex) {
            throw new RuntimeException(ex);
        } catch (InvocationTargetException ex) {
            throw ex.getCause();
        } catch (NoSuchMethodException ex) {
            throw new RuntimeException(ex);
        } catch (SecurityException ex) {
            throw new RuntimeException(ex);
        }

    }

    private final static int methodMoreSpecific(Method methodA, Method methodB) {
        Class[] argsA = methodA.getParameterTypes();
        Class[] argsB = methodB.getParameterTypes();
        for (int i = 0; i < argsA.length; i++) {
            if (argsA[i].isAssignableFrom(argsB[i])) {
                return 1;
            }
        }
        return -1;
    }
}

