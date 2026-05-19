#ifndef EVENT_H
#define EVENT_H

#include <string>
#include <vector>
#include <functional>
#include <memory>

class Event {
public:
    enum class Type {
        KEY_PRESS,
        KEY_RELEASE,
        MOUSE_MOVE,
        MOUSE_CLICK,
        UPDATE,
        RENDER,
        CUSTOM
    };
    
    Event(Type type) : type(type) {}
    virtual ~Event() = default;
    
    Type getType() const { return type; }
    
private:
    Type type;
};

class EventDispatcher {
public:
    template<typename T, typename F>
    void subscribe(F&& func) {
        auto subscriber = std::make_shared<std::function<void(const T&)>>(std::forward<F>(func));
        subscribers[static_cast<int>(T::eventType)].push_back(subscriber);
    }
    
    template<typename T>
    void dispatch(const T& event) {
        int eventType = static_cast<int>(T::eventType);
        for (const auto& subscriber : subscribers[eventType]) {
            (*std::static_pointer_cast<std::function<void(const T&)>>(subscriber))(event);
        }
    }
    
private:
    std::vector<std::vector<std::shared_ptr<void>>> subscribers;
};

#endif