#include <Adafruit_NeoPixel.h>

#define PIN 6
#define NUMPIXELS 60

Adafruit_NeoPixel pixels(NUMPIXELS, PIN, NEO_GRB + NEO_KHZ800);

String receivedCommand = "";

void setup()
{
  Serial.begin(9600);
  pixels.begin();
  pixels.show();
}

void loop()
{
  if (Serial.available() > 0)
  {
    char c = Serial.read();
    if (c == '%')
    {
      // Fin de commande, analyser
      parseCommand(receivedCommand);
      receivedCommand = "";
    }
    else {
      receivedCommand += c;
    }
  }
}

// Fonction pour analyser la commande
void parseCommand(String command)
{
  String type = getValue(command, "TYPE:");
  String speed = getValue(command, "SPEED:");
  String color = getValue(command, "COLOR:");

  int r = 0, g = 0, b = 0;
  if (color.length() > 0)
  {
    sscanf(color.c_str(), "%d,%d,%d", &r, &g, &b);
  }

  if (type == "CASCADE")
  {
    int delayTime = (speed == "FAST") ? 50 : 200; // Exemple : ajuster la vitesse
    fadeCascade(pixels.Color(r, g, b), delayTime);
  }
}

// Fonction utilitaire pour extraire une valeur par clé
String getValue(String data, String key)
{
  int start = data.indexOf(key);
  if (start == -1)
    return "";
  start += key.length();
  int end = data.indexOf('|', start);
  if (end == -1)
    end = data.length();
  return data.substring(start, end);
}

// Exemple d'animation cascade
void fadeCascade(uint32_t color, int delayTime)
{
  for (int i = 0; i < NUMPIXELS; i++)
  {
    pixels.setPixelColor(i, color);
    pixels.show();
    delay(delayTime);
    pixels.setPixelColor(i, 0); // Éteindre après le délai
  }
}
