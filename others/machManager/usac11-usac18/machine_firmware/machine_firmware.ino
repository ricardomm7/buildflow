#include <DHT.h>

#define DHTPIN 16        // Pino de dados do sensor DHT11
#define DHTTYPE DHT11    // Tipo do sensor DHT11
#define LED_PIN_TEMP 17  // Pino do LED para temperatura
#define LED_PIN_HUM 18   // Pino do LED para humidade
#define LED_PIN_CMD1 17  // Pino para LED 1 (controlado por comando)
#define LED_PIN_CMD2 18  // Pino para LED 2 (controlado por comando)
#define LED_PIN_CMD3 21  // Pino para LED 3 (controlado por comando)
#define LED_PIN_CMD4 22  // Pino para LED 4 (controlado por comando)
#define LED_PIN_CMD5 27  // Pino para LED 5 (controlado por comando)

DHT dht(DHTPIN, DHTTYPE);

int temp;
int hum;

void setup() {
  Serial.begin(115200);
  dht.begin();

  pinMode(LED_PIN_TEMP, OUTPUT);
  pinMode(LED_PIN_HUM, OUTPUT);
  pinMode(LED_PIN_CMD1, OUTPUT);
  pinMode(LED_PIN_CMD2, OUTPUT);
  pinMode(LED_PIN_CMD3, OUTPUT);
  pinMode(LED_PIN_CMD4, OUTPUT);
  pinMode(LED_PIN_CMD5, OUTPUT);
  pinMode(LED_BUILTIN, OUTPUT);
  digitalWrite(LED_PIN_CMD1, HIGH);  // Liga o LED1
  digitalWrite(LED_PIN_CMD2, HIGH);  // Liga o LED2
  digitalWrite(LED_PIN_CMD3, HIGH);  // Liga o LED3
  digitalWrite(LED_PIN_CMD4, HIGH);  // Liga o LED4
  digitalWrite(LED_PIN_CMD5, HIGH);  // Liga o LED5
  digitalWrite(LED_BUILTIN, HIGH);
  delay(1000);
  digitalWrite(LED_PIN_CMD1, LOW);  // Desliga o LED1
  digitalWrite(LED_PIN_CMD2, LOW);  // Desliga o LED2
  digitalWrite(LED_PIN_CMD3, LOW);  // desLiga o LED3
  digitalWrite(LED_PIN_CMD4, LOW);  // desLiga o LED4
  digitalWrite(LED_PIN_CMD5, LOW);  // desLiga o LED5
  digitalWrite(LED_BUILTIN, LOW);
  delay(1000);

  Serial.println("Setup complete. Waiting for commands and sensor data...");
}

String wait_for_command_from_mach_manager() {
  String command = "";

  //Serial.println("Waiting for command...");
  while (command.length() == 0) {
    if (Serial.available() > 0) {
      command = Serial.readStringUntil('\n');
    }
  }

  //Serial.print("Received command: ");
  //Serial.println(command);
  return command;
}

void turn_on_leds(String cmd) {
  if (cmd.startsWith("ON")) {
    digitalWrite(LED_BUILTIN, HIGH);
  }
  if (cmd.startsWith("OP")) {
    int state1 = cmd.charAt(3) - '0';
    int state2 = cmd.charAt(5) - '0';
    int state3 = cmd.charAt(7) - '0';
    int state4 = cmd.charAt(9) - '0';
    int state5 = cmd.charAt(11) - '0';

    digitalWrite(LED_PIN_CMD1, state1);
    digitalWrite(LED_PIN_CMD2, state2);
    digitalWrite(LED_PIN_CMD3, state3);
    digitalWrite(LED_PIN_CMD4, state4);
    digitalWrite(LED_PIN_CMD5, state5);

    for (int i = 0; i < 8; i++) {
        digitalWrite(LED_BUILTIN, HIGH);
        delay(250);
        digitalWrite(LED_BUILTIN, LOW);
        delay(250);
    }
  }
  if (cmd.startsWith("OFF")) {
    digitalWrite(LED_BUILTIN, LOW);
  }
}

void turn_off_leds() {
  digitalWrite(LED_PIN_CMD1, LOW);
  digitalWrite(LED_PIN_CMD2, LOW);
  digitalWrite(LED_PIN_CMD3, LOW);
  digitalWrite(LED_PIN_CMD4, LOW);
  digitalWrite(LED_PIN_CMD5, LOW);
  digitalWrite(LED_BUILTIN, LOW);
}

int read_temp_from_sensor() {
  float temperature = dht.readTemperature();
  if (isnan(temperature)) {
    //Serial.println("Failed to read temperature!");
    return -1;
  }
  //Serial.print("Temperature: ");
  //Serial.print(temperature);
  //Serial.println(" °C");
  return (int) temperature;
}

int read_hum_from_sensor() {
  float humidity = dht.readHumidity();
  if (isnan(humidity)) {
    //Serial.println("Failed to read humidity!");
    return -1;
  }
  //Serial.print("Humidity: ");
  //Serial.print(humidity);
  //Serial.println(" %");
  return (int) humidity;
}

void send_data(String data) {
  Serial.println(data);
}

void loop() {
  String cmd = wait_for_command_from_mach_manager();
  temp = read_temp_from_sensor();
  hum = read_hum_from_sensor();
  String str = "TEMP&unit:celsius&value:" + String(temp) + "#HUM&unit:percentage&value:" + String(hum);
  send_data(str);
  turn_on_leds(cmd);
  delay(2000);
  turn_off_leds();
}
