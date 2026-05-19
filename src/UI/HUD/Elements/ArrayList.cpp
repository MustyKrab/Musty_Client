#include "../HUD/ArrayList.h"
#include "../Renderer/Renderer.h"

ArrayList::ArrayList() {
    // Constructor
}

ArrayList::~ArrayList() {
    // Destructor
}

void ArrayList::update() {
    // Update array list
}

void ArrayList::render() {
    // Render array list
    float x = 10.0f;
    float y = 30.0f;
    float spacing = 20.0f;
    
    // Get all modules (in real implementation from module manager)
    // For demo: create some fake modules
    std::vector<std::pair<std::string, bool>> demoModules = {
        {"KillAura", true},
        {"Fly", false},
        {"Scaffold", true},
        {"Fullbright", false},
        {"Velocity", true},
        {"AntiFall", false},
        {"Speed", true},
        {"Step", false}
    };
    
    for (const auto& module : demoModules) {
        if (module.second) { // Only show toggled modules
            // Draw module name with shadow
            Renderer::drawShadowedText(module.first, x, y, 0.4f, 0.0f, 1.0f, 0.0f, 1.0f, false); // Green
            y += spacing;
        }
    }
}