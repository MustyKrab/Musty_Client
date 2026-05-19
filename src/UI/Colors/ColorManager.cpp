#include "ColorManager.h"
#include "../Renderer/Renderer.h"

ColorManager::ColorManager()
    : primaryColor(0.0f, 0.5f, 1.0f, 1.0f), // Blue
      secondaryColor(1.0f, 0.0f, 0.5f, 1.0f), // Pink
      opacity(0.9f) {}

ColorManager::~ColorManager() {}

void ColorManager::render() {
    // Render color picker panel
    float x = 100.0f, y = 100.0f;
    float width = 300.0f, height = 400.0f;
    
    // Draw panel
    Renderer::drawRoundedRect(x, y, width, height, 8.0f, 0.2f, 0.2f, 0.2f, 0.9f);
    
    // Draw title
    Renderer::drawText("Color Manager", x + 20, y + 20, 0.45f, 1.0f, 1.0f, 1.0f, false);
    
    // Render color picker
    renderColorPicker();
    
    // Render color presets
    renderColorPresets();
}

void ColorManager::update() {
    // Update color manager
}

void ColorManager::renderColorPicker() {
    float pickerX = 120.0f, pickerY = 150.0f;
    
    // Draw primary color picker
    Renderer::drawText("Primary Color", pickerX, pickerY - 20, 0.4f, 1.0f, 1.0f, 1.0f, false);
    Renderer::drawRoundedRect(pickerX, pickerY, 200, 200, 6.0f, primaryColor.x, primaryColor.y, primaryColor.z, primaryColor.w);
    
    // Draw secondary color picker
    Renderer::drawText("Secondary Color", pickerX + 250, pickerY - 20, 0.4f, 1.0f, 1.0f, 1.0f, false);
    Renderer::drawRoundedRect(pickerX + 250, pickerY, 200, 200, 6.0f, secondaryColor.x, secondaryColor.y, secondaryColor.z, secondaryColor.w);
}

void ColorManager::renderColorPresets() {
    float presetX = 120.0f, presetY = 400.0f;
    
    // Draw presets title
    Renderer::drawText("Presets", presetX, presetY - 20, 0.4f, 1.0f, 1.0f, 1.0f, false);
    
    // Draw preset buttons
    std::vector<std::pair<std::string, ImVec4>> presets = {
        {"Ocean", ImVec4(0.0f, 0.5f, 1.0f, 1.0f)},
        {"Fire", ImVec4(1.0f, 0.1f, 0.0f, 1.0f)},
        {"Nature", ImVec4(0.0f, 0.8f, 0.0f, 1.0f)},
        {"Royal", ImVec4(0.5f, 0.0f, 0.5f, 1.0f)}
    };
    
    for (size_t i = 0; i < presets.size(); ++i) {
        float x = presetX + (i % 2) * 110;
        float y = presetY + (i / 2) * 60;
        
        // Draw preset button
        Renderer::drawRoundedRect(x, y, 100, 50, 4.0f, presets[i].second.x, presets[i].second.y, presets[i].second.z, presets[i].second.w);
        Renderer::drawText(presets[i].first, x + 10, y + 15, 0.35f, 1.0f, 1.0f, 1.0f, false);
    }
}