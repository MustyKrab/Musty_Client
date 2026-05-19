#ifndef RENDERER_H
#define RENDERER_H

#include <SDL.h>
#include <SDL_ttf.h>
#include <SDL_image.h>
#include <glad/glad.h>
#include <imgui.h>
#include <string>
#include <vector>

class Renderer {
public:
    // Drawing functions
    static void drawRect(float x, float y, float width, float height, float r, float g, float b, float a);
    static void drawRoundedRect(float x, float y, float width, float height, float radius, float r, float g, float b, float a);
    static void drawRoundedRectWithGradient(float x, float y, float width, float height, float radius, 
                                            float r1, float g1, float b1, float a1,
                                            float r2, float g2, float b2, float a2);
    static void drawGradientRect(float x, float y, float width, float height, 
                                 float r1, float g1, float b1, float a1,
                                 float r2, float g2, float b2, float a2);
    static void drawLine(float x1, float y1, float x2, float y2, float thickness, float r, float g, float b, float a);
    static void drawTriangle(float x1, float y1, float x2, float y2, float x3, float y3, float r, float g, float b, float a);
    
    // Text rendering
    static void drawText(const std::string& text, float x, float y, float scale, float r, float g, float b, float a, bool centered);
    static void drawShadowedText(const std::string& text, float x, float y, float scale, float r, float g, float b, float a, bool centered);
    
    // Color utilities
    static ImVec4 hexToRgb(const std::string& hex);
    static std::string rgbToHex(float r, float g, float b);
    
    // Initialization
    static void init(SDL_Window* window);
    static void cleanup();
    
private:
    static TTF_Font* font;
    static SDL_Color sdlColor;
    static GLuint textureId;
    static int textureWidth;
    static int textureHeight;
};

#endif