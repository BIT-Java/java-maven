Wprowadzenie do Maven
---

### Czym jest Maven?

Maven to w skrócie narzędzie automatyzujące budowę oprogramowania na platformę Java.

---
### Jak działa Maven - cykle:

Wszystkie działania, które oferuje Maven są podzielone na 3 cykle - tzw. cykle życia - **clean**, **site**, **default**.

Cykl **clean** jest odpowiedzialny za wyczyszczenie wyników działania wcześniejszych operacji budowania (np. skompilowane klasy, itd.)

Cykl **site** tworzy dokumentację projektu.

Cykl **default** jest podzielony na następujące fazy:
* **validate** - sprawdzenie poprawności projektu
* **compile**  - kompilowanie kodu źródłowego
* **test** - wykonanie testów jednostkowych
* **package** - pakowanie skompilowanego kodu (do pliku `*.jar`, `*.war`)
* **integration-test** - 
* **verify** - sprawdzenie poprawności paczki
* **install** - umieszczenie paczki w lokalnym repozytorium
* **deploy** - publikacja paczki w zdalnym repozytorium

Fazy są wykonywane w podanej kolejności, tzn. że wykonanie fazy **package** spowoduje, że wcześniej zostaną
wykonane odpowiednio fazy **validate**, **compile** oraz **test**

Aby wykonać poszczególne cykle bądź fazy, wystarczy wykonać polecenie w terminalu, np.:
* `mvn clean`
* `mvn package`
* `mvn clean package`

---
### Plik `pom.xml`:

Plik `pom.xml` to plik, który określa sposób budowy aplikacji - tj. m.in. czy mają byc przeprowadzone testy,
w jaki sposób oraz które pliki mają zostać spakowane, itd.

**przykładowa struktura pliku `pom.xml`:**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>pl.mzlnk</groupId>
    <artifactId>MavenProject</artifactId>
    <version>1.0</version>
    <packaging>jar</packaging>
    <name>Maven Project</name>
    <url>https://github.com/BIT-Java/java-maven</url>
    
    <build>
        <!-- describe how project should be built -->
    </build>
    
    <repositories>
        <!-- add custom repositories where dependencies are from -->
    </repositories>

    <dependencies>
        <!-- add dependencies here -->
    </dependencies>

</project>
```

* `<groupId>` - twórca, zazwyczaj odwrócona nazwa domenowa (**wymagane**)
* `<artifacfId>` - nazwa projektu (**wymagane**)
* `<version>` - wersja projektu (**wymagane**)
* `<packaging>` - sposób pakowania - np. jar
* `<name>` - nazwa wyświetlana
* `<url>` - link do strony projektu
* `<build>` - miejsce, w którym określany jest sposób budowania projektu (np. poprzez dodanie odpowiednich pluginów)
* `<repositories>` - miejsce, w którym można umieścić namiary na wewnętrzne repozytoria, w których znajdują się wymagane w projekcie *dependencies*
* `<dependencies>` - miejsce, w którym można umieścić *dependencies*, z których korzysta projekt (np. *JUnit*, *Project Lombok*)

**przykładowa struktura `<repositories>`:**

```xml
<repositories>
    <repository>
        <id>repository-id</id>
        <url>https://example.com/repo</url>
    </repository>
</repositories>
```

**przykładowa struktura `<dependencies>`:**

```xml
<dependencies>
    <dependency>
        <groupId>pl.mzlnk</groupId>
        <artifactId>FileUtil</artifactId>
        <version>1.14</version>
    </dependency>
</dependencies>
```

---
### Budowanie projektu - atrybut `<build>`:

```xml
<build>
    <finalName>JavaMavenExample</finalName>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <version>3.8.1</version>
            <configuration>
                <source>13</source>
                <target>13</target>
            </configuration>
        </plugin>
        <plugin>
            <artifactId>maven-assembly-plugin</artifactId>
            <configuration>
                <archive>
                    <manifest>
                        <mainClass>pl.mzlnk.bitjava.mavenproject.Main</mainClass>
                    </manifest>
                </archive>
                <descriptorRefs>
                    <descriptorRef>jar-with-dependencies</descriptorRef>
                </descriptorRefs>
                <appendAssemblyId>false</appendAssemblyId>
            </configuration>
        </plugin>
    </plugins>
</build>
```

Pierwszy plugin - `maven-compiler-plugin` pozwala na konfigurację procesu kompilacji klas znajdujących się w projekcie. W tym przypadku
została ustawiona wersja Javy, która ma być wykorzystana przy kompilacji

Drugi plugin pozwala na spakowanie projektu do pliku wykonywalnego `*.jar`, ponieważ domyślnie faza **package** nie oferuje spakowania
całego projektu wraz ze wszystkimi *dependencies*, a także nie ustawia klasy startowej z metodą *main*, od której ma zostać uruchomiona aplikacja.

Aby zbudować projekt ze wszystkimi *dependencies* do pliku wykonywalnego `*.jar` przy pomocy tego pluginu, należy wykonać polecenie:

`mvn clean compile assembly:single`


---
### JUnit + Maven

Aby korzystać z testów oferowanych przez JUnit w projekcie Maven, należy:
 
1) dodać odpowiednie *dependencies* (w tym przypadku `junit-jupiter-params` oraz (**WAŻNE**) `junit-jupiter-engine`)
2) dodać plugin `maven-surefire-plugin`

Zatem poszczególne części pliku `pom.xml` powinny wyglądać następująco:

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-surefire-plugin</artifactId>
            <version>3.0.0-M1</version>
        </plugin>
    </plugins>
 </build>

<dependencies>
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter-params</artifactId>
        <version>5.6.0-M1</version>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter-engine</artifactId>
        <version>5.6.0-M1</version>
        <scope>test</scope>
    </dependency>
</dependencies>
```