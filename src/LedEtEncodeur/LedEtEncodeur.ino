// TO USE TYPE THE FOLOWING COMMAND IN THE MONITOR
// TYPE:BLOCK|SPEED:FAST|COLOR:255,5,5%

#include <Adafruit_NeoPixel.h>
#include <Encoder.h>

// Configuration des LEDs
#define PIN 6
#define NUMPIXELS 60
Adafruit_NeoPixel pixels(NUMPIXELS, PIN, NEO_GRB + NEO_KHZ800);

// Variables pour la communication série
String receivedCommand = "";

// Broches de l'encodeur rotatif
#define ENC_PIN_A 5
#define ENC_PIN_B 4
#define BTN_PIN 3

// Création de l'objet Encoder
Encoder myEnc(ENC_PIN_A, ENC_PIN_B);

// Intervalle de lissage (en ms)
const unsigned long samplingInterval = 500;
unsigned long lastSamplingTime = 0;

// Variable pour accumuler les variations
long deltaSum = 0;

// Position précédente
long oldPosition = 0;
unsigned long lastButtonPress = 0;
static int lastBtnState = HIGH;

// Parametres pour les animations
struct Animation
{
  uint32_t color;
  int delayTime;
  String type;
  unsigned long lastUpdate;
  bool active;
};

Animation currentAnimation;

void setup()
{
  // Setup pour les LEDs
  Serial.begin(9600);
  pixels.begin();
  pixels.show();

  pinMode(BTN_PIN, INPUT_PULLUP);
}

void loop()
{
  // Gestion des inputs via commandes série
  handleSerialInput();

  // Gestion de l'encodeur rotatif
  handleEncoder();

  // Gestions de l'animation des leds
  updateEffect();
}

// Gestion de l'entrée série et du contrôle des LEDs
void handleSerialInput()
{
  if (Serial.available() > 0)
  {
    char c = Serial.read();
    if (c == '%')
    {
      parseCommand(receivedCommand);
      receivedCommand = "";
    }
    else
    {
      receivedCommand += c;
    }
  }
}

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

  if (type == "rain")
  {
    int delayTime = (speed == "FAST") ? 50 : 200;
    startEffect(pixels.Color(r, g, b), delayTime, "rain");
  }
  else if (type == "jungle")
  {
    int delayTime = (speed == "FAST") ? 50 : 200;
    startEffect(pixels.Color(r, g, b), delayTime, "cloud");
  }
  else if (type == "fire")
  {
    int delayTime = (speed == "FAST") ? 30 : 100;
    startEffect(pixels.Color(r, g, b), delayTime, "fire");
  }
  else if (type == "space")
  {
    int delayTime = (speed == "FAST") ? 50 : 150;
    startEffect(pixels.Color(r, g, b), delayTime, "block");
  }
  else if (type == "stop") // Nouvelle commande pour éteindre toutes les LEDs
  {
    stopEffect(); // Appel de la fonction stop
  }
}

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

void updateEffect()
{
  if (currentAnimation.type == "rain")
  {
    updateRainEffect();
  }
  else if (currentAnimation.type == "cloud")
  {
    updateCloudEffect();
  }
  else if (currentAnimation.type == "fire")
  {
    updateFireEffect();
  }
  else if (currentAnimation.type == "block")
  {
    updateBlockEffect();
  }
}

void startEffect(uint32_t color, int delayTime, String type)
{
  currentAnimation.color = color;
  currentAnimation.delayTime = delayTime;
  currentAnimation.type = type;
  currentAnimation.lastUpdate = millis();
  currentAnimation.active = true;
}

void stopEffect()
{
  // Fonction pour éteindre toutes les LEDs
  pixels.clear();                  // Effacer toutes les LEDs
  pixels.show();                   // Appliquer l'effacement
  currentAnimation.active = false; // Désactiver l'animation en cours
}

void updateRainEffect()
{
  if (!currentAnimation.active && currentAnimation.type != "rain")
    return;

  static const int numDrops = 12;
  static int drops[numDrops];
  static bool initialized = false;

  if (!initialized)
  {
    for (int i = 0; i < numDrops; i++)
    {
      drops[i] = random(0, NUMPIXELS);
    }
    initialized = true;
  }

  if (millis() - currentAnimation.lastUpdate >= currentAnimation.delayTime)
  {
    for (int i = 0; i < numDrops; i++)
    {
      pixels.setPixelColor(drops[i], 0);
      drops[i] = (drops[i] + 1) % NUMPIXELS;
      pixels.setPixelColor(drops[i], currentAnimation.color);
    }
    pixels.show();
    currentAnimation.lastUpdate = millis();
  }
}

void updateCloudEffect()
{
  if (!currentAnimation.active || currentAnimation.type != "cloud")
    return;

  static const int numClouds = 5;
  static int cloudCenters[numClouds];
  static int cloudDirections[numClouds];
  static bool initialized = false;

  if (!initialized)
  {
    for (int i = 0; i < 5; i++)
    {
      cloudCenters[i] = random(0, NUMPIXELS);
      cloudDirections[i] = (random(0, 2) * 2 - 1); // Direction -1 ou +1
    }
    initialized = true;
  }

  if (millis() - currentAnimation.lastUpdate >= currentAnimation.delayTime)
  {
    pixels.clear();

    for (int i = 0; i < numClouds; i++)
    {
      int center = cloudCenters[i];

      // Dégradé autour du centre du nuage (jusqu'à 5 LEDs de chaque côté)
      for (int offset = -5; offset <= 5; offset++)
      {
        int pos = center + offset;
        if (pos >= 0 && pos < NUMPIXELS)
        {
          float intensity = max(0.0, 1.0 - abs(offset) / 5.0);
          pixels.setPixelColor(pos, adjustBrightness(currentAnimation.color, intensity));
        }
      }

      // Mise à jour de la position du centre
      cloudCenters[i] += cloudDirections[i];

      // Changement de direction si on atteint les bords
      if (cloudCenters[i] <= 0 || cloudCenters[i] >= NUMPIXELS - 1)
      {
        cloudDirections[i] *= -1;
      }
    }

    pixels.show();
    currentAnimation.lastUpdate = millis();
  }
}

void updateFireEffect()
{
  if (!currentAnimation.active || currentAnimation.type != "fire")
    return;

  if (millis() - currentAnimation.lastUpdate >= currentAnimation.delayTime)
  {
    float neibhor = 0;
    for (int i = 0; i < NUMPIXELS; i++)
    {
      // Variation aléatoire de l'intensité entre 50% et 100%
      float intensity = (neibhor + (random(0, 40) / 100.0));
      neibhor = intensity - neibhor;

      // Ajustement de la couleur vers des teintes rouges/oranges
      uint32_t flickerColor = adjustBrightness(currentAnimation.color, intensity);
      pixels.setPixelColor(i, flickerColor);
    }

    pixels.show();
    currentAnimation.lastUpdate = millis();
  }
}

void updateBlockEffect()
{
  if (!currentAnimation.active || currentAnimation.type != "block")
    return;

  static const int numBlocks = 5;              // Nombre de blocs
  static int blockPositions[numBlocks];        // Positions des blocs
  static float blockIntensities[numBlocks];    // Intensité de chaque bloc
  static unsigned long blockTimers[numBlocks]; // Horloge de chaque bloc
  static bool initialized = false;             // Variable pour initialiser une seule fois

  if (!initialized)
  {
    // Initialisation des positions des blocs et des timers
    for (int i = 0; i < numBlocks; i++)
    {
      blockPositions[i] = random(0, NUMPIXELS - 5); // Position aléatoire pour chaque bloc
      blockIntensities[i] = 0.0;                    // Initialiser l'intensité à 0
      blockTimers[i] = millis();                    // Initialiser le timer à l'heure actuelle
    }
    initialized = true;
  }

  if (millis() - currentAnimation.lastUpdate >= currentAnimation.delayTime)
  {
    pixels.clear(); // Effacer l'écran avant de redessiner

    for (int i = 0; i < numBlocks; i++)
    {
      // Allumer progressivement chaque bloc
      if (blockIntensities[i] < 1.0)
      {
        blockIntensities[i] += 0.05; // Augmenter l'intensité
      }

      // Allumer les 5 LEDs du bloc avec l'intensité actuelle
      for (int j = 0; j < 5; j++)
      {
        int pos = blockPositions[i] + j;
        if (pos < NUMPIXELS)
        {
          pixels.setPixelColor(pos, adjustBrightness(currentAnimation.color, blockIntensities[i]));
        }
      }
      if (blockIntensities[i] >= 1)
      {
        // Éteindre les 5 LEDs du bloc
        blockIntensities[i] -= 1; // Réduire rapidement l'intensité
        blockPositions[i] = random(0, NUMPIXELS - 5);
        blockTimers[i] = millis(); // Réinitialiser le timer pour cette action
      }
    }

    pixels.show();                          // Appliquer les modifications
    currentAnimation.lastUpdate = millis(); // Mettre à jour l'horloge
  }
}

uint32_t adjustBrightness(uint32_t color, float brightness)
{
  uint8_t r = (uint8_t)((color >> 16 & 0xFF) * brightness);
  uint8_t g = (uint8_t)((color >> 8 & 0xFF) * brightness);
  uint8_t b = (uint8_t)((color & 0xFF) * brightness);
  return pixels.Color(r, g, b);
}

// Gestion de l'encodeur rotatif
void handleEncoder()
{
  long newPosition = myEnc.read();
  long delta = newPosition - oldPosition;
  oldPosition = newPosition;

  // Accumule les variations
  deltaSum += delta;

  // Vérifie si l'intervalle de sampling est écoulé
  unsigned long currentTime = millis();
  if (currentTime - lastSamplingTime >= samplingInterval)
  {
    // Lissage terminé, on interprète le résultat
    if (deltaSum > 0)
    {
      Serial.print("U"); // Direction globale vers le haut
    }
    else if (deltaSum < 0)
    {
      Serial.print("D"); // Direction globale vers le bas
    }
    // Si deltaSum == 0, pas de mouvement significatif, on n'affiche rien

    // Reset des compteurs
    deltaSum = 0;
    lastSamplingTime = currentTime;
  }

  // Gestion du bouton
  int btnState = digitalRead(BTN_PIN);
  if (btnState != lastBtnState)
  {
    if (btnState == LOW && (millis() - lastButtonPress > 50))
    {
      Serial.print("C");
      lastButtonPress = millis();
    }
  }
  lastBtnState = btnState;
}
