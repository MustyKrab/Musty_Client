#include "Event.h"

// Event system implementation

// Define event types for specific events
struct KeyPressEvent : public Event {
    static constexpr Type eventType = Type::KEY_PRESS;
    
    KeyPressEvent(int key) : Event(Type::KEY_PRESS), key(key) {}
    int key;
};

struct KeyReleaseEvent : public Event {
    static constexpr Type eventType = Type::KEY_RELEASE;
    
    KeyReleaseEvent(int key) : Event(Type::KEY_RELEASE), key(key) {}
    int key;
};

struct MouseMoveEvent : public Event {
    static constexpr Type eventType = Type::MOUSE_MOVE;
    
    MouseMoveEvent(float x, float y) : Event(Type::MOUSE_MOVE), x(x), y(y) {}
    float x, y;
};

struct MouseClickEvent : public Event {
    static constexpr Type eventType = Type::MOUSE_CLICK;
    
    MouseClickEvent(int button, bool pressed) 
        : Event(Type::MOUSE_CLICK), button(button), pressed(pressed) {}
    int button;
    bool pressed;
};

struct UpdateEvent : public Event {
    static constexpr Type eventType = Type::UPDATE;
    
    UpdateEvent(float deltaTime) : Event(Type::UPDATE), deltaTime(deltaTime) {}
    float deltaTime;
};

struct RenderEvent : public Event {
    static constexpr Type eventType = Type::RENDER;
    
    RenderEvent() : Event(Type::RENDER) {}
};