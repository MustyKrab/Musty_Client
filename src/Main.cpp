#include <iostream>
#include <vector>
#include <memory>
#include <algorithm>

#include "ClickGUI.h"
#include "HUD.h"
#include "Settings.h"
#include "ColorManager.h"
#include "Renderer.h"
#include "Module.h"
#include "Event.h"

// Global variables for demo
std::vector<std::unique_ptr<Module>> modules;
std::unique_ptr<EventDispatcher> eventDispatcher;

void initModules() {
    // Create some demo modules
    modules.push_back(std::make_unique<MockModule>("KillAura"));
    modules.push_back(std::make_unique<MockModule>("Fly"));
    modules.push_back(std::make_unique<MockModule>("Scaffold"));
    modules.push_back(std::make_unique<MockModule>("Fullbright"));
    
    Logger::info("Demo modules initialized");
}

void initEvents() {
    // Create event dispatcher
    eventDispatcher = std::make_unique<EventDispatcher>();
    
    Logger::info("Event system initialized");
}

int main(int argc, char* argv[]) {
    // Initialize SDL
    if (SDL_Init(SDL_INIT_VIDEO | SDL_INIT_TIMER | SDL_INIT_GAMECONTROLLER) != 0) {
        std::cerr << "Error initializing SDL: " << SDL_GetError() << std::endl;
        return 1;
    }

    // Create window
    SDL_Window* window = SDL_CreateWindow(
        "Musty_Client",
        SDL_WINDOWPOS_CENTERED, SDL_WINDOWPOS_CENTERED,
        1280, 720,
        SDL_WINDOW_OPENGL | SDL_WINDOW_RESIZABLE
    );

    // Setup SDL_GL context
    SDL_GLContext gl_context = SDL_GL_CreateContext(window);
    SDL_GL_MakeCurrent(window, gl_context);
    SDL_GL_SetSwapInterval(1); // Enable vsync

    // Initialize GLAD
    if (!gladLoadGLLoader((GLADloadproc)SDL_GL_GetProcAddress)) {
        std::cerr << "Failed to initialize GLAD" << std::endl;
        return 1;
    }

    // Setup OpenGL viewport
    glViewport(0, 0, 1280, 720);
    glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

    // Initialize SDL_image
    if (!(IMG_Init(IMG_INIT_PNG | IMG_INIT_JPG) & (IMG_INIT_PNG | IMG_INIT_JPG))) {
        std::cerr << "SDL_image could not initialize! SDL_image Error: " << IMG_GetError() << std::endl;
        return 1;
    }

    // Initialize SDL_ttf
    if (TTF_Init() == -1) {
        std::cerr << "SDL_ttf could not initialize! SDL_ttf Error: " << TTF_GetError() << std::endl;
        return 1;
    }

    // Setup Dear ImGui context
    IMGUI_CHECKVERSION();
    ImGui::CreateContext();
    ImGuiIO& io = ImGui::GetIO(); (void)io;
    
    // Setup Dear ImGui style
    ImGui::StyleColorsDark();
    
    // Setup Platform bindings
    ImGui_ImplSDL2_InitForOpenGL(window, gl_context);
    ImGui_ImplOpenGL3_Init("#version 130");

    // Initialize renderer
    Renderer::init(window);

    // Initialize modules and events
    initModules();
    initEvents();

    // Initialize UI components
    ClickGUI clickGUI;
    HUD hud;
    Settings settings;
    ColorManager colorManager;
    
    // Main loop
    bool showClickGUI = false;
    bool quit = false;
    SDL_Event event;
    
    while (!quit) {
        // Poll and handle events
        while (SDL_PollEvent(&event)) {
            ImGui_ImplSDL2_ProcessEvent(&event);
            
            if (event.type == SDL_QUIT || (event.type == SDL_KEYDOWN && event.key.keysym.sym == SDLK_ESCAPE)) {
                quit = true;
            }
            
            if (event.type == SDL_KEYDOWN && event.key.keysym.sym == SDLK_RSHIFT) {
                showClickGUI = !showClickGUI;
                if (showClickGUI) {
                    clickGUI.open();
                } else {
                    clickGUI.close();
                }
            }
        }

        // Start the Dear ImGui frame
        ImGui_ImplOpenGL3_NewFrame();
        ImGui_ImplSDL2_NewFrame(window);
        ImGui::NewFrame();

        // Render UI components
        if (showClickGUI) {
            clickGUI.render();
        }
        
        hud.render();
        settings.render();
        colorManager.render();
        
        // Update UI components
        hud.update();
        settings.update();
        colorManager.update();
        
        // Rendering
        ImGui::Render();
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        glClear(GL_COLOR_BUFFER_BIT);

        // Render ImGui
        ImGui_ImplOpenGL3_RenderDrawData(ImGui::GetDrawData());

        SDL_GL_SwapWindow(window);
    }

    // Cleanup
    Renderer::cleanup();
    
    ImGui_ImplOpenGL3_Shutdown();
    ImGui_ImplSDL2_Shutdown();
    ImGui::DestroyContext();

    SDL_GL_DeleteContext(gl_context);
    SDL_DestroyWindow(window);
    SDL_Quit();

    return 0;
}