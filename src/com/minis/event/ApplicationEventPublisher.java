package com.minis.event;

public interface ApplicationEventPublisher {
    void publishApplicationEvent(ApplicationEvent applicationEvent);
}
