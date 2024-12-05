#include <DHT.h>

#define DHTPIN 16            // Pino de dados do sensor DHT11
#define DHTTYPE DHT11        // Tipo do sensor DHT11
#define LED_PIN_TEMP 17      // Pino do LED para temperatura
#define LED_PIN_HUM 18       // Pino do LED para humidade
#define LED_PIN_CMD1 19      // Pino para LED 1 (controlado por comando)
#define LED_PIN_CMD2 20      // Pino para LED 2 (controlado por comando)

DHT dht(DHTPIN, DHTTYPE);

float temp;
float hum;

void setup() {
    Serial.begin(115200);
    dht.begin();

    pinMode(LED_PIN_TEMP, OUTPUT);
    pinMode(LED_PIN_HUM, OUTPUT);
    pinMode(LED_PIN_CMD1, OUTPUT);
    pinMode(LED_PIN_CMD2, OUTPUT);

    Serial.println("Setup complete. Waiting for commands and sensor data...");
}

String wait_for_command_from_mach_manager() {
    String command = "";

    Serial.println("Waiting for command...");
    while (command.length() == 0) {
        if (Serial.available() > 0) {
            command = Serial.readStringUntil('\n'); // Lê até encontrar uma nova linha
        }
    }

    Serial.print("Received command: ");
    Serial.println(command);
    return command;
}

// Funções para manipular os LEDs com base no comando recebido
void turn_on_leds(String cmd) {
    if (cmd.startsWith("ON")) {
        int state1 = cmd.charAt(3) - '0';
        int state2 = cmd.charAt(5) - '0';

        digitalWrite(LED_PIN_CMD1, state1);
        digitalWrite(LED_PIN_CMD2, state2);

        Serial.print("LED_CMD1: ");
        Serial.println(state1 ? "ON" : "OFF");
        Serial.print("LED_CMD2: ");
        Serial.println(state2 ? "ON" : "OFF");
    }
}

void turn_off_leds() {
    digitalWrite(LED_PIN_CMD1, LOW);
    digitalWrite(LED_PIN_CMD2, LOW);
    Serial.println("All LEDs turned off.");
}

// Leitura da temperatura
float read_temp_from_sensor() {
    float temperature = dht.readTemperature();
    if (isnan(temperature)) {
        Serial.println("Failed to read temperature!");
        return -1;
    }
    Serial.print("Temperature: ");
    Serial.print(temperature);
    Serial.println(" °C");
    return temperature;
}

// Leitura da umidade
float read_hum_from_sensor() {
    float humidity = dht.readHumidity();
    if (isnan(humidity)) {
        Serial.println("Failed to read humidity!");
        return -1;
    }
    Serial.print("Humidity: ");
    Serial.print(humidity);
    Serial.println(" %");
    return humidity;
}

// Envio de dados formatados
void send_data(String data) {
    Serial.print("Sending data: ");
    Serial.println(data);
}

void loop() {
    String cmd = wait_for_command_from_mach_manager();

    // Leitura de temperatura e umidade
    temp = read_temp_from_sensor();
    hum = read_hum_from_sensor();

    if (temp != -1 && hum != -1) {
        String str = "TEMP&unit:celsius&value:" + String(temp) + "#HUM&unit:percentage&value:" + String(hum);
        send_data(str);
    }

    // Controle dos LEDs com base no comando
    turn_on_leds(cmd);
    delay(2000); // Simula as operações realizadas por 2 segundos

    // Desliga os LEDs
    turn_off_leds();
}
