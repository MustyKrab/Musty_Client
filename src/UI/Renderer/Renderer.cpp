#include "Renderer.h"
#include <SDL.h>
#include <SDL_ttf.h>
#include <glad/glad.h>
#include <imgui.h>
#include <algorithm>
#include <cmath>

// Static members initialization
TTF_Font* Renderer::font = nullptr;
SDL_Color Renderer::sdlColor = {255, 255, 255, 255};
GLuint Renderer::textureId = 0;
int Renderer::textureWidth = 0;
int Renderer::textureHeight = 0;

void Renderer::init(SDL_Window* window) {
    // Initialize SDL_ttf
    if (TTF_Init() == -1) {
        std::cerr << "SDL_ttf could not initialize! SDL_ttf Error: " << TTF_GetError() << std::endl;
        return;
    }
    
    // Load font (Roboto, 16pt)
    font = TTF_OpenFont("resources/fonts/Roboto.ttf", 16);
    if (!font) {
        std::cerr << "Failed to load font! SDL_ttf Error: " << TTF_GetError() << std::endl;
        font = TTF_OpenFont("resources/fonts/DejaVuSans.ttf", 16); // Fallback
        if (!font) {
            std::cerr << "Failed to load fallback font! SDL_ttf Error: " << TTF_GetError() << std::endl;
            return;
        }
    }
    
    // Set font style
    TTF_SetFontStyle(font, TTF_STYLE_BOLD | TTF_STYLE_ITALIC);
}

void Renderer::cleanup() {
    if (textureId) {
        glDeleteTextures(1, &textureId);
        textureId = 0;
    }
    
    if (font) {
        TTF_CloseFont(font);
        font = nullptr;
    }
    
    TTF_Quit();
}

void Renderer::drawRect(float x, float y, float width, float height, float r, float g, float b, float a) {
    // Enable blending
    glEnable(GL_BLEND);
    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    
    // Set color
    glColor4f(r, g, b, a);
    
    // Draw rectangle
    glBegin(GL_QUADS);
        glVertex2f(x, y);
        glVertex2f(x + width, y);
        glVertex2f(x + width, y + height);
        glVertex2f(x, y + height);
    glEnd();
    
    // Disable blending
    glDisable(GL_BLEND);
}

void Renderer::drawRoundedRect(float x, float y, float width, float height, float radius, float r, float g, float b, float a) {
    // Enable blending
    glEnable(GL_BLEND);
    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    
    // Set color
    glColor4f(r, g, b, a);
    
    // Draw rounded rectangle
    glBegin(GL_QUADS);
        // Top-left corner
        for (int i = 0; i <= 90; i += 5) {
            float angle = (i * M_PI / 180.0f) - (M_PI / 2.0f);
            glVertex2f(x + radius + cos(angle) * radius, y + radius + sin(angle) * radius);
        }
        
        // Top edge
        glVertex2f(x + width - radius, y);
        
        // Top-right corner
        for (int i = 0; i <= 90; i += 5) {
            float angle = (i * M_PI / 180.0f);
            glVertex2f(x + width - radius + cos(angle) * radius, y + radius + sin(angle) * radius);
        }
        
        // Right edge
        glVertex2f(x + width, y + height - radius);
        
        // Bottom-right corner
        for (int i = 0; i <= 90; i += 5) {
            float angle = (i * M_PI / 180.0f) + (M_PI / 2.0f);
            glVertex2f(x + width - radius + cos(angle) * radius, y + height - radius + sin(angle) * radius);
        }
        
        // Bottom edge
        glVertex2f(x + radius, y + height);
        
        // Bottom-left corner
        for (int i = 0; i <= 90; i += 5) {
            float angle = (i * M_PI / 180.0f) + M_PI;
            glVertex2f(x + radius + cos(angle) * radius, y + height - radius + sin(angle) * radius);
        }
        
        // Left edge
        glVertex2f(x, y + radius);
        
        // Top-left corner (completion)
        for (int i = 0; i <= 90; i += 5) {
            float angle = (i * M_PI / 180.0f) + (3 * M_PI / 2.0f);
            glVertex2f(x + radius + cos(angle) * radius, y + radius + sin(angle) * radius);
        }
    glEnd();
    
    // Disable blending
    glDisable(GL_BLEND);
}

void Renderer::drawRoundedRectWithGradient(float x, float y, float width, float height, float radius, 
                                           float r1, float g1, float b1, float a1,
                                           float r2, float g2, float b2, float a2) {
    // Enable blending
    glEnable(GL_BLEND);
    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    
    // Draw rounded rectangle with gradient
    glBegin(GL_QUADS);
        // Top-left corner
        for (int i = 0; i <= 90; i += 5) {
            float angle = (i * M_PI / 180.0f) - (M_PI / 2.0f);
            glColor4f(r1, g1, b1, a1);
            glVertex2f(x + radius + cos(angle) * radius, y + radius + sin(angle) * radius);
        }
        
        // Top edge
        glColor4f(r1, g1, b1, a1);
        glVertex2f(x + width - radius, y);
        
        // Top-right corner
        for (int i = 0; i <= 90; i += 5) {
            float angle = (i * M_PI / 180.0f);
            glColor4f(r2, g2, b2, a2);
            glVertex2f(x + width - radius + cos(angle) * radius, y + radius + sin(angle) * radius);
        }
        
        // Right edge
        glColor4f(r2, g2, b2, a2);
        glVertex2f(x + width, y + height - radius);
        
        // Bottom-right corner
        for (int i = 0; i <= 90; i += 5) {
            float angle = (i * M_PI / 180.0f) + (M_PI / 2.0f);
            glColor4f(r2, g2, b2, a2);
            glVertex2f(x + width - radius + cos(angle) * radius, y + height - radius + sin(angle) * radius);
        }
        
        // Bottom edge
        glColor4f(r2, g2, b2, a2);
        glVertex2f(x + radius, y + height);
        
        // Bottom-left corner
        for (int i = 0; i <= 90; i += 5) {
            float angle = (i * M_PI / 180.0f) + M_PI;
            glColor4f(r1, g1, b1, a1);
            glVertex2f(x + radius + cos(angle) * radius, y + height - radius + sin(angle) * radius);
        }
        
        // Left edge
        glColor4f(r1, g1, b1, a1);
        glVertex2f(x, y + radius);
        
        // Top-left corner (completion)
        for (int i = 0; i <= 90; i += 5) {
            float angle = (i * M_PI / 180.0f) + (3 * M_PI / 2.0f);
            glColor4f(r1, g1, b1, a1);
            glVertex2f(x + radius + cos(angle) * radius, y + radius + sin(angle) * radius);
        }
    glEnd();
    
    // Disable blending
    glDisable(GL_BLEND);
}

void Renderer::drawGradientRect(float x, float y, float width, float height, 
                                float r1, float g1, float b1, float a1,
                                float r2, float g2, float b2, float a2) {
    // Enable blending
    glEnable(GL_BLEND);
    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    
    // Draw gradient rectangle
    glBegin(GL_QUADS);
        glColor4f(r1, g1, b1, a1);
        glVertex2f(x, y);
        
        glColor4f(r2, g2, b2, a2);
        glVertex2f(x + width, y);
        
        glColor4f(r2, g2, b2, a2);
        glVertex2f(x + width, y + height);
        
        glColor4f(r1, g1, b1, a1);
        glVertex2f(x, y + height);
    glEnd();
    
    // Disable blending
    glDisable(GL_BLEND);
}

void Renderer::drawLine(float x1, float y1, float x2, float y2, float thickness, float r, float g, float b, float a) {
    // Enable blending
    glEnable(GL_BLEND);
    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    
    // Set line width
    glLineWidth(thickness);
    
    // Set color
    glColor4f(r, g, b, a);
    
    // Draw line
    glBegin(GL_LINES);
        glVertex2f(x1, y1);
        glVertex2f(x2, y2);
    glEnd();
    
    // Reset line width
    glLineWidth(1.0f);
    
    // Disable blending
    glDisable(GL_BLEND);
}

void Renderer::drawTriangle(float x1, float y1, float x2, float y2, float x3, float y3, float r, float g, float b, float a) {
    // Enable blending
    glEnable(GL_BLEND);
    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    
    // Set color
    glColor4f(r, g, b, a);
    
    // Draw triangle
    glBegin(GL_TRIANGLES);
        glVertex2f(x1, y1);
        glVertex2f(x2, y2);
        glVertex2f(x3, y3);
    glEnd();
    
    // Disable blending
    glDisable(GL_BLEND);
}

void Renderer::drawText(const std::string& text, float x, float y, float scale, float r, float g, float b, float a, bool centered) {
    if (!font) return;
    
    // Enable blending
    glEnable(GL_BLEND);
    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    
    // Set color
    SDL_Color color = {static_cast<Uint8>(r * 255), static_cast<Uint8>(g * 255), static_cast<Uint8>(b * 255), static_cast<Uint8>(a * 255)};
    
    // Render text to surface
    SDL_Surface* surface = TTF_RenderUTF8_Blended(font, text.c_str(), color);
    if (!surface) {
        std::cerr << "Failed to render text: " << TTF_GetError() << std::endl;
        return;
    }
    
    // Convert to texture
    GLuint texture;
    glGenTextures(1, &texture);
    glBindTexture(GL_TEXTURE_2D, texture);
    
    int mode = GL_RGBA;
    glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, surface->w, surface->h, 0, GL_RGBA, GL_UNSIGNED_BYTE, surface->pixels);
    
    // Set texture parameters
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
    
    // Calculate dimensions
    float width = static_cast<float>(surface->w) * scale;
    float height = static_cast<float>(surface->h) * scale;
    
    // Draw textured quad
    glColor4f(1.0f, 1.0f, 1.0f, a);
    glBegin(GL_QUADS);
        glTexCoord2f(0, 0); glVertex2f(x - (centered ? width / 2 : 0), y - height);
        glTexCoord2f(1, 0); glVertex2f(x + width - (centered ? width / 2 : 0), y - height);
        glTexCoord2f(1, 1); glVertex2f(x + width - (centered ? width / 2 : 0), y);
        glTexCoord2f(0, 1); glVertex2f(x - (centered ? width / 2 : 0), y);
    glEnd();
    
    // Clean up
    glDeleteTextures(1, &texture);
    SDL_FreeSurface(surface);
    
    // Disable blending
    glDisable(GL_BLEND);
}

void Renderer::drawShadowedText(const std::string& text, float x, float y, float scale, float r, float g, float b, float a, bool centered) {
    // Draw shadow first
    drawText(text, x + 2, y - 2, scale, 0.0f, 0.0f, 0.0f, a * 0.5f, centered);
    
    // Draw main text
    drawText(text, x, y, scale, r, g, b, a, centered);
}

ImVec4 Renderer::hexToRgb(const std::string& hex) {
    // Convert hex color to RGB
    // Implementation here
    return ImVec4(1.0f, 1.0f, 1.0f, 1.0f);
}

std::string Renderer::rgbToHex(float r, float g, float b) {
    // Convert RGB to hex
    // Implementation here
    return "#FFFFFF";
}