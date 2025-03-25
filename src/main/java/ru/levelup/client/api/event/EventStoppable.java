package ru.levelup.client.api.event;

    public abstract class EventStoppable extends Event {
        private boolean stopped;
        public void stop(){
            stopped = true;
        }
        public boolean isStopped(){
            return stopped;
        }
    }

