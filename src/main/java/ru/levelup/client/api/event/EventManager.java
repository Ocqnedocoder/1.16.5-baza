package ru.levelup.client.api.event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class EventManager {
    private static final Map<Class<? extends Event>, ArrayList<Data>> REGISTRY_MAP = new HashMap();
    public EventManager() {
    }

    private static void sortListValue(Class<? extends Event> clazz) {
        ArrayList<Data> flexibleArray = new ArrayList();
        byte[] var2 = Priority.VALUE_ARRAY;
        int var3 = var2.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            byte b = var2[var4];
            Iterator var6 = ((ArrayList)REGISTRY_MAP.get(clazz)).iterator();

            while(var6.hasNext()) {
                Data methodData = (Data)var6.next();
                if (methodData.priority == b) {
                    flexibleArray.add(methodData);
                }
            }
        }

        REGISTRY_MAP.put(clazz, flexibleArray);
    }

    private static boolean isMethodBad(Method method) {
        return method.getParameterTypes().length != 1 || !method.isAnnotationPresent(EventHendler.class);
    }

    private static boolean isMethodBad(Method method, Class<? extends Event> clazz) {
        return isMethodBad(method) || method.getParameterTypes()[0].equals(clazz);
    }

    public static ArrayList get(Class<? extends Event> clazz) {
        return REGISTRY_MAP.get(clazz);
    }

    public static void cleanMap(boolean removeOnly) {
        Iterator<Map.Entry<Class<? extends Event>, ArrayList<Data>>> iterator = REGISTRY_MAP.entrySet().iterator();

        while(true) {
            do {
                if (!iterator.hasNext()) {
                    return;
                }
            } while(removeOnly && !((ArrayList)((Map.Entry)iterator.next()).getValue()).isEmpty());

            iterator.remove();
        }
    }

    public static void unregister(Object o, Class<? extends Event> clazz) {
        if (REGISTRY_MAP.containsKey(clazz)) {
            Iterator var2 = (new ArrayList((Collection)REGISTRY_MAP.get(clazz))).iterator();

            while(var2.hasNext()) {
                Data methodData = (Data)var2.next();
                if (methodData.source.equals(o)) {
                    ((ArrayList)REGISTRY_MAP.get(clazz)).remove(methodData);
                }
            }
        }

        cleanMap(true);
    }

    public static void unregister(Object o) {
        Iterator var1 = (new ArrayList(REGISTRY_MAP.values())).iterator();

        while(var1.hasNext()) {
            ArrayList<Data> flexArray = (ArrayList)var1.next();

            for(int i = flexArray.size() - 1; i >= 0; --i) {
                if (((Data)flexArray.get(i)).source.equals(o)) {
                    flexArray.remove(i);
                }
            }
        }

        cleanMap(true);
    }

    public static void register(Method method, Object o) {
        Class<?> clazz = method.getParameterTypes()[0];
        Data methodData = new Data(o, method, ((EventHendler)method.getAnnotation(EventHendler.class)).value());
        if (!methodData.target.isAccessible()) {
            methodData.target.setAccessible(true);
        }

        if (!REGISTRY_MAP.containsKey(clazz)) {
            REGISTRY_MAP.put((Class<? extends Event>) clazz, new ArrayList());
        }

        ArrayList<Data> dataList = (ArrayList)REGISTRY_MAP.get(clazz);
        if (!dataList.contains(methodData)) {
            dataList.add(methodData);
            sortListValue((Class<? extends Event>) clazz);
        }

    }

    public static void register(Object o, Class<? extends Event> clazz) {
        Method[] var2 = o.getClass().getMethods();
        int var3 = var2.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            Method method = var2[var4];
            if (!isMethodBad(method, clazz)) {
                register(method, o);
            }
        }

    }

    public static void register(Object o) {
        Method[] var1 = o.getClass().getMethods();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            Method method = var1[var3];
            if (!isMethodBad(method)) {
                register(method, o);
            }
        }

    }
    public static Event call(Event event) {
        final Map<Class<? extends Event>, List<MethodData>> REGISTRY_MAP = new  HashMap<>();

        List<MethodData> dataList = REGISTRY_MAP.get(event.getClass());

        if (dataList != null) {
            if (event instanceof EventStoppable) {
                EventStoppable stoppable = (EventStoppable) event;

                for (final MethodData data : dataList) {
                    invoke(data, event);

                    if (stoppable.isStopped()) {
                        break;
                    }
                }
            } else {
                for (final MethodData data : dataList) {
                    invoke(data, event);
                }
            }
        }

        return event;
    }
    private static void invoke(MethodData data, Event argument) {
        try {
            data.getTarget().invoke(data.getSource(), argument);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ignored) {
        }
    }

    private static final class MethodData {
        private final Object source;
        private final Method target;
        private final byte priority;

        public MethodData(Object source, Method target, byte priority){
            this.source = source;
            this.target = target;
            this.priority = priority;
        }

        public Object getSource() {
            return source;
        }

        public Method getTarget() {
            return target;
        }

        public byte getPriority() {
            return priority;
        }

    }
}
