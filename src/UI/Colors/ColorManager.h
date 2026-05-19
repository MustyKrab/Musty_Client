#include <vector>
#include <string>
#include <algorithm>

class ColorManager {
public:
    ColorManager();
    ~ColorManager();
    
    void render();
    void update();
    
    ImVec4 getPrimaryColor() const { return primaryColor; }
    ImVec4 getSecondaryColor() const { return secondaryColor; }
    float getOpacity() const { return opacity; }
    
    void setPrimaryColor(const ImVec4& color) { primaryColor = color; }
    void setSecondaryColor(const ImVec4& color) { secondaryColor = color; }
    void setOpacity(float newOpacity) { opacity = newOpacity; }
    
private:
    ImVec4 primaryColor;
    ImVec4 secondaryColor;
    float opacity;
    
    void renderColorPicker();
    void renderColorPresets();
};

#endif