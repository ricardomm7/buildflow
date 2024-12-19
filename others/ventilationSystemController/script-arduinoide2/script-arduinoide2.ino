#include <DHT.h>

// Pinos
#define DHTPIN 16
#define DHTTYPE DHT11
#define EXHAUST_FAN_PIN 18
#define VENTILATION_FAN_PIN 17
#define MQ2_PIN 27

// Constantes para o sensor MQ2
const float RL_VALUE = 5.0;  // Resistência de carga em kΩ
const float RO_CLEAN_AIR_FACTOR = 9.83;  // Fator para ar limpo
const float VOLT_RESOLUTION = 3.3;  // Tensão de referência do ADC
const int ADC_BITS = 12;  // Resolução do ADC do Pico
const float ADC_MAX = (1 << ADC_BITS) - 1;  // Valor máximo do ADC (4095 para 12 bits)

DHT dht(DHTPIN, DHTTYPE);

float temp;
float inicialTemp;
float inicialHum;
float hum;
float gas;
float inicialGas;
float Ro = 10;

unsigned long previousMillis = 0;
const unsigned long interval = 60000;

float lastTemp;
float lastHum;
float lastGas;

float readMQ2Raw() {
  float adc = analogRead(MQ2_PIN);
  float voltage = (adc * VOLT_RESOLUTION) / ADC_MAX;
  float rs = ((VOLT_RESOLUTION * RL_VALUE) / voltage) - RL_VALUE;
  return rs;
}

float calculatePPM(float rs) {
  float ratio = rs / Ro;
  return pow(10, ((log10(ratio) - 0.47) / -0.284));
}

void calibrateMQ2() {
  Serial.println("Calibrating MQ2 sensor...");
  float rs_air = 0;
  
  for(int i = 0; i < 50; i++) {
    rs_air += readMQ2Raw();
    delay(500);
  }
  rs_air = rs_air / 50.0;
  
  Ro = rs_air / RO_CLEAN_AIR_FACTOR;
  
  Serial.print("Ro = ");
  Serial.println(Ro);
}

void setup() {
  Serial.begin(115200);
  
  pinMode(EXHAUST_FAN_PIN, OUTPUT);
  pinMode(VENTILATION_FAN_PIN, OUTPUT);
  pinMode(MQ2_PIN, INPUT);
  
  dht.begin();
  Serial.println("DHT sensor started");
  
  delay(2000);
  
  inicialTemp = dht.readTemperature();
  inicialHum = dht.readHumidity();
  
  if (isnan(inicialTemp) || isnan(inicialHum)) {
    Serial.println("Failed to read initial DHT values!");
  } else {
    Serial.print("Initial Temperature = ");
    Serial.print(inicialTemp);
    Serial.println(" °C");
    Serial.print("Initial Humidity = ");
    Serial.print(inicialHum);
    Serial.println(" %");
  }
  
  Serial.println("Waiting for MQ2 warmup...");
  delay(2000);
  calibrateMQ2();
  
  inicialGas = calculatePPM(readMQ2Raw());
  Serial.print("Initial Gas PPM = ");
  Serial.println(inicialGas);
}

void turnOnExhaustFan(int duration) {
  digitalWrite(EXHAUST_FAN_PIN, HIGH);
  delay(duration * 1000);
  digitalWrite(EXHAUST_FAN_PIN, LOW);
}

void turnOnVentilationFan(int duration) {
  digitalWrite(VENTILATION_FAN_PIN, HIGH);
  delay(duration * 1000);
  digitalWrite(VENTILATION_FAN_PIN, LOW);
}

void turnOnBothFans(int duration) {
  digitalWrite(EXHAUST_FAN_PIN, HIGH);
  digitalWrite(VENTILATION_FAN_PIN, HIGH);
  delay(duration * 1000);
  digitalWrite(EXHAUST_FAN_PIN, LOW);
  digitalWrite(VENTILATION_FAN_PIN, LOW);
}

void loop() {
  unsigned long currentMillis = millis();
  
  temp = dht.readTemperature();
  hum = dht.readHumidity();
  gas = calculatePPM(readMQ2Raw());
  
  // Imprimir valores para debug
  Serial.print("Temperature: "); Serial.print(temp); Serial.println(" °C");
  Serial.print("Humidity: "); Serial.print(hum); Serial.println(" %");
  Serial.print("Gas PPM: "); Serial.println(gas);
  
  // Detectar variações súbitas
  if (currentMillis - previousMillis >= interval) {
    previousMillis = currentMillis;
    
    if (abs(temp - lastTemp) >= 0.3 * lastTemp) {
      Serial.println("Sudden temperature change detected!");
      turnOnBothFans(10);
    }
    
    if (abs(hum - lastHum) >= 0.3 * lastHum) {
      Serial.println("Sudden humidity change detected!");
      turnOnBothFans(10);
    }
    
    if (abs(gas - lastGas) >= 0.3 * lastGas) {
      Serial.println("Sudden gas change detected!");
      turnOnBothFans(10);
    }
    
    lastTemp = temp;
    lastHum = hum;
    lastGas = gas;
  }
  
  // Verificar limiares
  if (temp >= inicialTemp + 5.0) {
    Serial.println("Temperature threshold exceeded.");
    turnOnExhaustFan(5);
    turnOnVentilationFan(5);
  }
  
  if (hum >= inicialHum + inicialHum * 0.05) {
    Serial.println("Humidity threshold exceeded.");
    turnOnVentilationFan(10);
    turnOnExhaustFan(10);
  }
  
  if (gas >= inicialGas + inicialGas * 0.02) {
    Serial.println("Gas threshold exceeded.");
    turnOnBothFans(10);
  }
  
  delay(2000);
}