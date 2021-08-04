# Vizsgaremek

## Leírás

Jelen vizsgaremek témájához az apropót az adta, hogy készülőben van egy valós szoftver, 
amely a jövőben meg fogja könnyíteni a Struktúraváltó tanfolyamok során a résztvevők 
előrehaladásának ellenőrzését, tárolását, lekérdezését, áttekintését. Ennek egy részét 
fogja képezni ez a projekt is, amely az oktatók és az általuk megtartott konzultációk 
kezelését teszi lehetővé.

---

## Használata

### Mentor

A `Mentor` entitás a következő attribútumokkal rendelkezik:

* `id` (az oktató egyedi azonosítója)
* `name` (az oktató neve, nem lehet `null` és üres, valamint legfeljebb 255 karakter)
* `email` (az oktató e-mail címe, nem lehet `null` és üres, valamint legfeljebb 255 karakter)
* `position` (az oktató pozíciója, ez egy enum a következő értékekkel: `SENIOR_MENTOR`, `MENTOR`, `JUNIOR_MENTOR`)
* `status` (az oktató státusza, ez is egy enum a következő értékekkel: `ACTIVE`, `PENDING`, `DELETED`)
* `consultations` (ez egy lista, amelyben az oktató által tartott konzultációk találhatóak)

A következő végpontokon érjük el az entitást:

| HTTP metódus | Végpont                 | Leírás                                                                 |
| ------------ | ----------------------- | ---------------------------------------------------------------------- |
| GET          | `"/api/mentors"`        | lekérdezi az összes oktatót                                            |
| GET          | `"/api/mentors/{id}"`   | lekérdez egy oktatót `id` alapján                                      |
| POST         | `"/api/mentors"`        | létrehoz egy oktatót                                                   |
| PUT          | `"/api/mentors/{id}"`   | módosít egy oktatót `id` alapján                                       |
| DELETE       | `"/api/mentors/{id}"`   | `id` alapján úgy módosít egy oktatót, hogy a státusza `DELETED` legyen |

---

### Consultation

A `Consultation` entitás a következő attribútumokkal rendelkezik:

* `id` (a konzultáció egyedi azonosítója)
* `title` (a konzultáció címe)
* `time` (a konzultáció időpontja, `LocalDateTime` típus)
* `mentor` (a konzultációt tartó oktató)
* `subject` (a konzultáció tárgya, bővebb leírása)

A `Mentor` és a `Consultation` entitások között kétirányú, 1-n kapcsolat van.

A következő végpontokon érjük el az entitást:

| HTTP metódus | Végpont                              | Leírás                                                                         |
| ------------ | ------------------------------------ | ------------------------------------------------------------------------------ |
| GET          | `"/api/consultations"`               | lekérdezi az összes konzultációt                                               |
| GET          | `"/api/consultations/{id}"`          | lekérdez egy adott konzultációt `id` alapján                                   |
| POST         | `"/api/consultations"`               | létrehoz egy konzultációt és hozzárendeli egy oktatóhoz                        |
| PUT          | `"/api/consultations/{id}"`          | módosít egy adott konzultációt `id` alapján                                    |
| DELETE       | `"/api/consultations/{id}"`          | töröl egy adott konzultációt `id` alapján                                      |
| GET          | `"/api/consultations/mentor/{id}"`   | lekérdezi egy adott oktatóhoz tartozó konzultációkat az oktató `id`-ja alapján |

---