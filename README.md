# fae-draussen-ortung


## Function
![alt text][https://github.com/Archi-Lab/fae-draussen-ortung/blob/master/assests/route_1.png]
![alt text][https://github.com/Archi-Lab/fae-draussen-ortung/blob/master/assests/route_2.png]
![alt text][https://github.com/Archi-Lab/fae-draussen-ortung/blob/master/assests/route_3.png]


## Install and Run

Um den Service zu starten und mit kafka zu verbinden muss zu erst das Projekt mit maven geaubt werden.
Hierzu benötigt man den folgenden Befehl: 
```bash
mvn package -DskipTests=true
```
Maven erzeugt nun eine .Jar File des Projekts und legt diese im /target Ordner ab. 
Als nächstes kann mit hilfe der docker-compose Datei der Service gestartet werden. 
```bash
docker-compose up 
```
Zu beachten hierbei ist das zuerst der Messagebroker gestartet werden muss,
da sonnst ein Fehler in docker erscheint und der Start des Services nicht funktioniert.
