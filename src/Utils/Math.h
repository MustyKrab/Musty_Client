#ifndef MATH_UTILS_H
#define MATH_UTILS_H

#include <cmath>
#include <algorithm>

class MathUtils {
public:
    static float clamp(float value, float min, float max) {
        return std::max(min, std::min(value, max));
    }
    
    static float lerp(float a, float b, float t) {
        return a + (b - a) * t;
    }
    
    static float map(float value, float fromMin, float fromMax, float toMin, float toMax) {
        return toMin + (value - fromMin) * (toMax - toMin) / (fromMax - fromMin);
    }
    
    static float distance(float x1, float y1, float x2, float y2) {
        float dx = x2 - x1;
        float dy = y2 - y1;
        return std::sqrt(dx * dx + dy * dy);
    }
    
    static float angle(float x1, float y1, float x2, float y2) {
        return std::atan2(y2 - y1, x2 - x1);
    }
};