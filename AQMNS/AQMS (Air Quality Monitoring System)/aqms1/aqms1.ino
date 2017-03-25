/*
Team: Anak_TK
Projek AQMNS( Air Quality Monitoring and Navigation System)
by : Virbyansah Achmadan N.
file name : aqms1.ino
Description : To open this file you can use Arduino ide. In AQMNS project use Arduino Uno as a open source microcontroller.
ESP8266 as a modul wifi for communication between arduino with Acces Point or Mobile Hotspot. Source link for ESP8266 cabling : https://i.stack.imgur.com/KYbW1.png. 
GPS Module GY-GPS6MV2 for find the location coordinates of the vehincle. Source link for Gy-GPS6mV2 : https://hackspark.fr/media/catalog/product/a/r/arduinogps.png 
but we change Tx pin from 3 to 11 and Rx pin from 4 to 10. And MQ-7 as CO sensor. Source link for Mq-7 cabling http://www.learningaboutelectronics.com/Articles/MQ-7-carbon-monoxide-sensor-circuit-with-arduino.php
All of them connected into Arduino Uno using jumper cable and project board.
*/
#include <TinyGPS++.h> // add libary tinyGPS++ into Arduino IDE, you can download in this link http://arduiniana.org/libraries/tinygpsplus/ 
#include <SoftwareSerial.h> //software serial library 
 
const byte rxPin = 1;   // Wire this to Tx Pin of ESP8266 
const byte txPin = 0;   // Wire this to Rx Pin of ESP8266
const byte rxPins=10;   // Wire this to Tx Pin of GY-GPS6MV2
const byte txPins=11;   // Wire this to Rx Pin of GY-GPS6MV2
int sensorPin = A0;     // Wire this to Ao Pin of MQ-75
int sensorValue = 0;    // Set 0 sensorValue 
String panjang;         // Set String variable of panjang
String lats,lngs;       // Set String variable of lats and lngs
 
// We'll use a software serial interface to connect to ESP8266 
SoftwareSerial ESP8266 (rxPin, txPin); 
SoftwareSerial tes (rxPins, txPins);      //Rx=pin 1 Tx=pin 0
//SoftwareSerial serial_connection(4, 3); //Rx=pin 10, Tx=pin 11
TinyGPSPlus gps;                          //Set object gps
 
void setup() {    
 ESP8266.begin(115200); // Change this to the baudrate used by ESP8266 
 //Serial.println("GPS Start");
 delay(1000); 
 ESP8266.write("AT+RST\r\n");  // reset the ESP8266
 delay(5000); 
 ESP8266.write("AT+CWQAP\r\n"); // Forget current access point 
 delay(5000); 
 ESP8266.write("AT+CWJAP=\"Mi\",\"asdfqwert\"\r\n");  //connect into Access Point with Mi (name of SSID wifi) asdfqwert (password of wifi)  
 delay(10000);
 tes.begin(9600); //Change this to the baudrate used by  GY-GPS6MV2 GPS
 delay(10000); 
 //ESP8266.println("wifi sudah terkoneksi");
} 
 
void loop() {  
 while(tes.available())           //While there are characters to come from the GPS
  {
    gps.encode(tes.read());       // This feeds the serial NMEA data into the library one char at a time
  }
  if (gps.location.isUpdated()){  // This will pretty much be fired all the time anyway but will at least reduce it to only after a package of NMEA data comes in
  ESP8266.write("AT+CIPSTART=\"TCP\",\"192.168.43.37\",80\r\n"); // connect to 192.168.43.37 (server ip) and port server 80 (default port) 
  delay(10000);
  ESP8266.println("terkoneksi ke server");   //the server is connected
  sensorValue = analogRead(sensorPin);      // read MQ-7 sensor 
  delay(1000); 
  String data = String (sensorValue,DEC); //convert int MQ-7 value sensor to string because it will send into header http request 
  ESP8266.println("sensor="+data);       // print sensor value of MQ-7
  delay(1000);
  lats =String (gps.location.lat(), 6); // get latitude coordinate and convert into string 
  ESP8266.println(lats); // print latitude value
  lngs =String (gps.location.lng(), 6); // get latitude coordinate and convert into string
  ESP8266.println(lngs); // print latitude value
  panjang = "GET /aqms/insert.php?sensor="+data+"&lat="+lats+"&lng="+lngs;  // header http request 
  delay(1000); 
  ESP8266.print("AT+CIPSEND="); //send header http request
  ESP8266.println(panjang.length()+2);  // long of chracter panjang + 2 
  delay(1000);
  ESP8266.println("GET /aqms/insert.php?sensor="+data+"&lat="+lats+"&lng="+lngs); // print header http request 
  delay(1000);
 }
  
  } 

