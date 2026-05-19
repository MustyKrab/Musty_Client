#include "Logger.h"
#include <iostream>
#include <iomanip>

// Initialize static members
std::ofstream Logger::logFile;

void Logger::log(LogLevel level, const std::string& message) {
    // Open log file (append mode)
    if (!logFile.is_open()) {
        logFile.open("Musty_Client.log", std::ios::app);
        if (!logFile.is_open()) {
            std::cerr << "Failed to open log file" << std::endl;
            return;
        }
    }
    
    // Get timestamp
    std::string timestamp = getTimestamp();
    std::string levelStr = levelToString(level);
    
    // Log to file
    logFile << "[" << timestamp << "] [" << levelStr << "] " << message << std::endl;
    logFile.flush();
    
    // Also log to console
    std::cout << "[" << timestamp << "] [" << levelStr << "] " << message << std::endl;
}

void Logger::debug(const std::string& message) {
    log(DEBUG, message);
}

void Logger::info(const std::string& message) {
    log(INFO, message);
}

void Logger::warning(const std::string& message) {
    log(WARNING, message);
}

void Logger::error(const std::string& message) {
    log(ERROR, message);
}

std::string Logger::getTimestamp() {
    std::time_t now = std::time(nullptr);
    std::tm* timeinfo = std::localtime(&now);
    
    std::ostringstream oss;
    oss << std::put_time(timeinfo, "%Y-%m-%d %H:%M:%S");
    return oss.str();
}

std::string Logger::levelToString(LogLevel level) {
    switch (level) {
        case DEBUG: return "DEBUG";
        case INFO: return "INFO";
        case WARNING: return "WARNING";
        case ERROR: return "ERROR";
        default: return "UNKNOWN";
    }
}