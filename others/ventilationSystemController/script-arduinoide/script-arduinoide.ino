#include <DHT.h>

#define DHTPIN 16            // Pino de dados do sensor DHT11
#define DHTTYPE DHT11        // Tipo do sensor DHT11
#define LED_PIN_TEMP 17      // Pino do LED para temperatura
#define LED_PIN_HUM 18       // Pino do LED para humidade

DHT dht(DHTPIN, DHTTYPE);

float temp;
float inicialTemp;
float inicialHum;
float hum;

bool estadoLedTemp = LOW; // Estado atual do LED de temperatura
bool estadoLedHum = LOW;  // Estado atual do LED de humidade

void setup() {
  Serial.begin(115200);
  dht.begin();
  pinMode(LED_PIN_TEMP, OUTPUT);
  pinMode(LED_PIN_HUM, OUTPUT);
  Serial.println("DHT sensor started, reading temperature and humidity...");
  
  delay(2000);

  inicialTemp = dht.readTemperature();
  inicialHum = dht.readHumidity();
  
  delay(10000);

  if (isnan(inicialTemp) || isnan(inicialHum)) {
    Serial.println("Failed to read the initial values of the DHT sensor!");
  } else {
    Serial.print("Initial Temperature = ");
    Serial.print(inicialTemp);
    Serial.println(" °C");
    Serial.print("Initial Humidity = ");
    Serial.print(inicialHum);
    Serial.println(" %");
  }
}

void loop() {
  temp = dht.readTemperature();
  hum = dht.readHumidity();

  if (isnan(temp) || isnan(hum)) {
    Serial.println("Failed to read DHT sensor!");
  } else {
    Serial.print("Temperature = ");
    Serial.print(temp);
    Serial.println(" °C");
    Serial.print("Humidity = ");
    Serial.print(hum);
    Serial.println(" %");
  }

  if (temp >= inicialTemp + 5.0) {
    digitalWrite(LED_PIN_TEMP, HIGH);
    Serial.println("The temperature light is on.");
  } else {
    digitalWrite(LED_PIN_TEMP, LOW);
    Serial.println("The temperature light is off.");
  }

  if (hum >= inicialHum + inicialHum*0.05) {
    digitalWrite(LED_PIN_HUM, HIGH);
    Serial.println("The humidity light is on.");
  } else {
    digitalWrite(LED_PIN_HUM, LOW);
    Serial.println("The humidity light is off.");
  }
  
  delay(30000);
}