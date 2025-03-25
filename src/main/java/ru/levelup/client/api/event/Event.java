package ru.levelup.client.api.event;

import java.util.*;

public abstract class Event
{
    private boolean cancelled;

    public Event() {
        cancelled = false;
    }

    public Event call() {
        this.cancelled = false;
        List<Data> dataList = EventManager.get(this.getClass());
        if (dataList != null) {
            List<Data> copyDataList = new ArrayList(dataList);
            Iterator var3 = copyDataList.iterator();

            while(var3.hasNext()) {
                Data data = (Data) var3.next();

                try {
                    data.target.invoke(data.source, this);
                } catch (Exception var6) {
                    var6.printStackTrace();
                }
            }
        }
        return this;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
