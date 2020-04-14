# Lockbox Storage Lockers
> Radi se o web aplikaciji koja olakšava rad skladišta (skladišnih jedinica). Posjetiocima stranice se nudi pregled skladišta, trenutno slobodnih skladišnih jedinica, cjenovnik, i sistem za zakupninu skladišne jedinice. Korisnicima se nudi pregled svojih trenutno zakupljenih skladišnih jedinica. Nude se skladišta različitih kapaciteta (pa i cijena). Uposlenicima se nudi opcija dodavanja novih skladišnih jedinica, u slučaju proširivanja skladišta.

## Pokretanje
1. U service-registration-and-discovery-service projektu pokrenuti [Eureka server](service-registration-and-discovery-service/src/main/java/com/nwt/serviceregistrationanddiscoveryservice/ServiceRegistrationAndDiscoveryServiceApplication.java) za registraciju servisa
2. U ostala četiri projekta pokrenuti [servis za notifikacije](Notifications/src/main/java/com/nwt/notifications/NotificationsApplication.java), [servis za naplatu](Billings/src/main/java/com/nwt/billings/BillingsApplication.java), [servis za upravljanje klijentima](usercontrol/src/main/java/com/nwt/usercontrol/UsercontrolApplication.java) i [servis za upravljanje skladištima](storagecontrol/src/main/java/com/nwt/storagecontrol/StoragecontrolApplication.java)   
3. Nakon pokretanja svih aplikacija moguće je praviti API pozive na servise. Pozivanje API-ja za notifikacije se vrši na localhost portu 8080, API-ja za naplatu na localhost portu 8081, API-ja za upravljanje klijentima na localhost portu 8082, te API-ja za upravljanje skladištima na localhost portu 8083.
  
**Napomena: Prije pokretanja servisa potrebno je osigurati da su lokalni portovi 8080, 8081, 8082, 8083 i 8761 slobodni**

Dokumentacija API poziva svih servisa se nalazi na sljedećem [linku](https://drive.google.com/drive/u/1/folders/15kWRg2Xz1RkCOJllQ7k5LrisLRp52Fo9)
