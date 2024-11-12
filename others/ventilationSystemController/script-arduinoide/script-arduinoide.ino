#include <DHT.h>

#define DHTPIN 16            // Pino de dados do sensor DHT11
#define DHTTYPE DHT11        // Tipo do sensor DHT11
#define LED_PIN_TEMP 17      // Pino do LED para temperatura
#define LED_PIN_HUM 18       // Pino do LED para umidade

DHT dht(DHTPIN, DHTTYPE);

float temp;
float inicialTemp;
float inicialHum;
float hum;

void setup() {
  Serial.begin(115200);
  dht.begin();
  pinMode(LED_PIN_TEMP, OUTPUT);
  pinMode(LED_PIN_HUM, OUTPUT);
  Serial.println("Sensor DHT iniciado, lendo temperatura e humidade...");
  
  delay(2000);

  inicialTemp = dht.readTemperature();
  inicialHum = dht.readHumidity();
  
  if (isnan(inicialTemp) || isnan(inicialHum)) {
    Serial.println("Falha ao ler os valores iniciais do sensor DHT!");
  } else {
    Serial.print("Temperatura Inicial = ");
    Serial.print(inicialTemp);
    Serial.println(" °C");
    Serial.print("Humidade Inicial = ");
    Serial.print(inicialHum);
    Serial.println(" %");
  }
}

void loop() {
  temp = dht.readTemperature();
  hum = dht.readHumidity();

  if (isnan(temp) || isnan(hum)) {
    Serial.println("Falha ao ler do sensor DHT!");
  } else {
    Serial.print("Temperatura = ");
    Serial.print(temp);
    Serial.println(" °C");
    Serial.print("Humidade = ");
    Serial.print(hum);
    Serial.println(" %");
  }

  // Verifica aumento de 5 °C na temperatura
  if (temp >= inicialTemp + 5.0) {
    digitalWrite(LED_PIN_TEMP, HIGH);
  } else {
    digitalWrite(LED_PIN_TEMP, LOW);
  }

  if (hum >= inicialHum + inicialHum*0.05) {
    digitalWrite(LED_PIN_HUM, HIGH);
  } else {
    digitalWrite(LED_PIN_HUM, LOW);
  }
  
  delay(5000);
}
