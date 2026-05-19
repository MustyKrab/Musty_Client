#include "../HUD/TextRadar.h"
#include "../Renderer/Renderer.h"

TextRadar::TextRadar() {
    // Constructor
}

TextRadar::~TextRadar() {
    // Destructor
}

void TextRadar::update() {
    // Update text radar
}

void TextRadar::render() {
    // Render text radar
    float x = 200.0f;
    float y = 200.0f;
    
    // Draw text radar background
    Renderer::drawRoundedRect(x, y, 200, 150, 8.0f, 0.2f, 0.2f, 0.2f, 0.8f);
    
    // Draw title
    Renderer::drawText("TextRadar", x + 20, y + 10, 0.4f, 1.0f, 1.0f, 1.0f, 1.0f, false);
    
    // Draw players (demo)
    std::vector<std::string> players = {"Player1", "Player2", "Player3"};
    float textY = y + 40;
    
    for (const auto& player : players) {
        Renderer::drawText(player, x + 10, textY, 0.35f, 1.0f, 1.0f, 1.0f, 1.0f, false);
        textY += 25;
    }
    
    // Draw status
    Renderer::drawText("Players nearby: " + std::to_string(players.size()), x + 10, textY + 10, 0.3f, 1.0f, 1.0f, 1.0f, 1.0f, false);
}