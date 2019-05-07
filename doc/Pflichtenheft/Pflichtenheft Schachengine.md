# Pflichtenheft Schachengine



## 1 Zielbestimmung

Die Schachengine ist ein Programm, das ein Schachspieler simuliert und gegen das ein Benutzer Schach spielen kann, angebunden an einer Schach-GUI.

### 1.1 Musskriterien

- Der Benutzer:

  - kann die Engine über die Konsole starten und bedienen
  - kann die Engine vor dem Start und während des Spiels konfigurieren

- Die Schachengine:

  - kann im Modus Blitzschach (5 Minuten) spielen 
  - Das Niveau der Schachengine soll sich mit einen Schachspieler mit der Elo-Zahl 1800 vergleichen lassen.
  - kann alle für die Engine relevanten Schachregeln einhalten 


### 1.2 Wunschkriterien

- Die Schachengine soll sich an eine Endspiel-Datenbank anbinden lassen können

### 1.3 Abgrenzungskriterien

- keine besonderen Schachvarianten
- keine Frameworks oder Bibliotheken spezifisch für Schachengines verwenden
- kein Eröffnungsburch verwenden



## 2 Produkteinsatz

### 2.1 Andwendungsbereiche

Die Schachengine wird in Kombination mit einer Schachoberfläche verwendet, um primär die Schachart Blitzschach (5 Minuten) zu spielen.

### 2.2 Zielgruppe

Diese Schachengine ist für einen fortgeschrittenen Schachspieler gedacht, der aktiv noch Schach spielt und auch in einem Schachclub o. ä. vertreten ist.

### 2.3 Betriebsbedingungen

Es ist davon auszugehen, dass der Nutzer jederzeit das Programm beschaffen und starten kann. 

Es soll außerdem kombiniert mit einer Schachoberfläche laufen.



## 3 Produktumgebung

### 3.1 Software

- Java-Laufzeitumgebung (Version __11__)
- Windows (mindestens Version __Windows 7__), Mac OS (mindestens Version  __Mac OS 10.10__) oder eine beliebige Linux-Distribution (mindestens Linux-Kernel 3.x)
- Bevorzugte Schachoberfläche: Arena GUI

### 3.2 Hardware

- Ein Handelsüblicher Büro-Rechner:
  - Arbeitsspeicher: __4 Gigabyte__
  - ein __x86__ - Prozessor mit min. __2 Prozessorkernen__
  - Sekundärspeicher: SSD mit min. 128 Gigabyte
  
  

## 4 Produktfunktionen

### 4.1 Spieler (und Schachoberfläche)

#### /F0010/ Ein Spiel starten

__Akteure:__ Schachengine, Benutzer, Oberfläche

__Beschreibung:__ Der Benutzer oder die Schachoberfläche kann die Engine initialisieren und auch starten.

#### /F0020/ Schachengine bedienen

__Akteure:__ Schachengine, Benutzer, Oberfläche

__Beschreibung:__ Der Benutzer oder die Oberfläche kann der Engine über die Standardeingabe sagen, wann es nach einem optimalen Schachzug suchen darf und wann Sie stoppen soll.

#### /F0030/ Engine konfigurieren

__Akteure:__ Schachengine, Benutzer, Oberfläche

__Beschreibung:__ Der Benutzer oder die Oberfläche kann vor dem Spielstart Parameter, die die Engine vorgibt, ändern. Zudem kann die initiale Spielposition beeinflußt werden (um z.B alten Spielstand wiederherzustellen).



### 4.2 Schachengine

#### /F0110/ Mit einer Schachoberfläche kommunizieren

__Akteure:__ Schachengine, Schachoberfläche

__Beschreibung:__ Die Schachengine kann von einer Schachoberfläche durch Befehle, die dem UCI-Protokoll konform sind bedienen lassen. 

#### /F0120/ Nach dem derzeit besten Schachzug suchen

__Akteure:__ Schachengine, Benutzer, Schachoberfläche

__Beschreibung:__ Die Schachengine soll auf Kommando den besten Schachzug in der momentanen Schachposition ersuchen können.

#### /F0130/ Die Suche nach dem besten Schachzug stoppen

__Akteure:__ Schachengine, Benutzer, Schachoberfläche

__Beschreibung:__ Die Schachengine soll auf Kommando die momentane Suche nach dem besten Schachzug stoppen können. Die Suche soll auch automatisch abgebrochen werden können je nachdem wie das Zeitlimit eingestellt wurde. Dabei soll die Engine den aktuell Ihr bekannten besten Zug zurückgeben können.



## 5 Produktdaten

Die Schachengine soll für den Benutzer Log-Dateien hinterlassen, in denen die gesamte Standardausgabe von der Engine und der Schachoberfläche (mit Ausnahme der Suche nach dem bestem Schachzug) formattiert hineingeschrieben wird.

Außerdem wird gegebenenfalls ein Zugriff auf eine Endspiel-Datenbank bestehen, aus der Endspieldaten gelesen werden können. 



## 6 Produktleistungen

__/L100/__ Die Schachengine darf pro Zug die vorkonfigurierte Maximalschätzungszeit nicht überschreiten und soll immer einen Schachzug ausliefern können.

__/L200/__ Die Schachengine muss sich intern um ein optimales Zeitmanagement kümmern, so dass die Zeit bis zum Zeitlimit bei jeden Schachzug optimal genutzt wird.

__/L300/__ Es werden immer nur valide Schachzüge für alle Schachfiguren in Erwägung gezogen

__/L400/__ Alle Regeln, die für ein offizielles Schachspiel gelten müssen auch von der Schachengine eingehalten werden.

__/L500/__ Die Suche nach dem bestmöglichen Schachzuges soll effizient sein. Wenn Java in diesem Fall zu begrenzt ist, so wird ein Java Native Interface in einer tiefere Sprache wie C++ dazuentwickelt.

__/L600/__ Die Engine soll das komplette UCI-Protokoll mit Ausnahme der optionalen Schachart "Chess960" implementieren. 



## 7 Produktoberfläche

Die Bedienung geschieht entweder über eine Konsole mit den per UCI-Protokoll definierten Befehlen, oder durch die Schachoberfläche. 



## 8 Qualitätsbestimmungen

|                        | sehr wichtig | wichtig | weniger wichtig |
| ---------------------- | :----------: | :-----: | :-------------: |
| Robustheit             |      x       |         |                 |
| Zuferlässigkeit        |      x       |         |                 |
| Korrektheit            |      x       |         |                 |
| Benutzerfreundlichkeit |              |         |        x        |
| Effizienz              |      x       |         |                 |
| Portierbarkeit         |              |         |        x        |
| Kompatibilität         |              |    x    |                 |



## 9 Entwicklungsumgebung

### 9.1 Software

- Tools: 
  - Java JDK: Version 11
  - Falls benötigt: GCC/CC
  - IDE: IntelliJ



## 10 Ergänzung

### Fragen

- Soll der Nutzer das Zeitlimit der Schachzugevaluierung während des Spieles ändern dürfen?

- Soll die Engine auch Daten in die Endspieldatenbank schreiben, oder soll aus der Endspieldatenbank nur Daten gelesen werden?

  

## 11 Glossar

### 11.1 Begriffserklärungen

- Blitzschach: Form des Schachspiels mit einer Bedenkzeit von wenigern als 10 Minuten.
- Elo: Die Darstellung der Spielstärke eines Schachspielers als eine Zahl
- Für die Schachengine relevanten Regeln: 
- Endspiel: Die Endphase einer Parte, wenn nur noch wenige Figurenarten auf den Brett sind.



### 11.2 Links / Referenzen

[1]: <https://www.fide.com/fide/handbook.html?id=207&view=article>	"Schachregeln"
[2]: http://download.shredderchess.com/div/uci.zip	"UCI Protokoll"
[3]: <http://www.playwitharena.com/>	"Arena GUI (Link geht momentan nicht...?)"
[4]: https://de.wikipedia.org/wiki/Blitzschach
[5]: https://de.wikipedia.org/wiki/Endspiel_(Schach)
[6]: <https://de.wikipedia.org/wiki/Elo-Zahl>

