# Prooviülesanne CGI jaoks
Eesmärgiks täiendada CGI koodi, et luua töötavat lihtsat hambaarsti visiidi haldamise rakendust.

## Paigaldamise juhend

* Tõmmata repo alla
* Importida IDE'sse (võib kasutada CGI enda juhendit, mis antakse prooviülesandele kaasa)
* Jooksutada "DentistAppApplication" (port, millelt rakendus on saadaval ilmub konsooli)

## Tähelepanekud

* Väikse visiitide hulga kiireks sisestamiseks külastada aadressit <rakenduse_pealehe_aadress>/addDemoVisits, näiteks kui rakendus jookseb pordil 8080, siis http://localhost:8080/addDemoVisits.
See lisab andmebaasi 7 visiidikirjet testimiseks
* Projektil on olemas javadoc dokumentatsioon, asub kaustas doc. Käivitamiseks tõmmata alla ja mõnda html faili (näiteks index.html)

## Tööprotsess

### Etapp 1
#### 1.1
* Kestvus 5 minutit
* Probleeme ei esinenud, imporditud IntelliJ keskkonda

### 1.2
* Kestvus umbes 2 tundi (millest umbes 45 minutit läks projektiga tutvumiseks ja arusaamiseks, mis ja kuidas töötab)
* Tekkis arusaamise küsimus: rakendusel esialgu juba kuupäeva valik, palutakse lisada 2 uut sisendvälja: perearsti nime ning kellaaja ja kuupäeva valiku.
Kuna kuupäev juba olemas, siis lihtsalt lisasin kellaaja valiku - kokku rakendusel siis 4 sisendvälja visiidi broneerimisel.

### Etapp 2
#### 2.1
* Kestvus umbes 30 minutit
* Läks kiiresti, kuna alles hiljuti tegin rakendust, kus oli samuti vaja kuvada vaadet teatud registreeringutega. Lahendatatud tabeli abil
(vaatele antakse ette kõik broneeringud ning tsüklis lisatakse need tabeli ridadeks)

### Etapp 3
#### 3.1 
* Kestvus 2 tundi
* Nii suur ajakulu on seotud asjaoluga, et üritasin mitmel eri viisil asja tööle saada, millest üks siis ei õnnestunud. Lisaks kulus aega mõistmiseks,
kuidas teha mitmest otsingukriteeriumist ühisosa võtmist. Sai lahendatud kõikide kirjete võtmisega ning üleliigsete eemaldamisega neist.

### Etapp 4
#### 4.1
* Kestvus 15 minutit
* Mingeid probleeme ei esinenud, kontrollerisse lisatud meetod, mis kutsub dentistVisitService meetodi välja, mis omakorda DentistVisitDao,
milles broneering kustutatakse.

#### 4.2
* Kestvus 1.5 tundi. 
* Läksid sassi omavahel DentistVisitEntity ja DentistVisitDao, mille tõttu tuli segadus vaates.
* Lisaks kulus aega sellele, et leida, kuidas entityManager saab olemasoleva kirja üle kirjutada

####4.3
* Kestvus kuni 1 tund
* Tõsiseid probleeme sellega ei esinenud, "time.before" ja "time.after" lubasid lihtsasti realiseerida funktsionaalsuse
* Visiidi aknaks valitud 45 minutit, teisisõnu visiidist eespool alla 45 minutit visiidid võimatud (kuna muidu satuks visiit tagant olemasolevale
visiidile otsa) ning samuti tagapool kuni 45 minutit.

###Lisaks
* Umbes 1 tund kasutajaliidese "ilusamaks" muutmiseks, kasutatud bootstrap. Pom failis olid vajalikud "dependency'd" olemas, ei pidanud neid lisama
* Bugide parandamine kuni 30 minutit, leitud bugid: 1) olemasoleva visiidi muutmisel ütles rakendus, et visiidi aeg juba hõivatud (selle sama visiidi poolt 
tegelikult), lahenduseks id atribuudi kasutamine kontrollis, kui id't "üksteist segavatel" visiitidel ühtivad, siis tegu on ühe visiidiga ning võib broneeringu teha; 
2) visiidi kustutamisel visiidi muutmise vaatest, tekkis tõrkeid, seotud väikse näpuveaga.
