Az ételrendelő appunk összeköti az éhes felhasználókat a környékbeli éttermekkel. Böngészhetsz különböző éttermek kínálatában, leadhatod a rendelésed, és követheted annak állapotát. #SpeedySpoon

1. Felhasználói regisztráció, profilkezelés
•	Egyszerű regisztrációs folyamat (email, jelszó, név)
•	Profil testreszabása (profilkép, szállítási címek tárolása)
•	Felhasználói adatok kezelése és módosítása
2. Éttermek és ételek böngészése
•	Éttermek listázása kategóriák szerint (konyha típusa, értékelések, távolság)
•	Keresési és szűrési lehetőségek (ár, értékelés)
•	Étterem profilok megtekintése (nyitvatartás, menü, árak, értékelések)
•	Étel kategóriák szerinti rendezés a könnyebb keresés érdekében
3. Rendelési folyamat
•	Egyszerű kosár kezelés (hozzáadás, eltávolítás, mennyiség módosítása)
•	Gyors újrarendelés korábbi rendelésekből
•	Szállítási cím kiválasztása vagy új cím megadása
4. Rendelés nyomon követése
•	Valós idejű rendelés státusz követés
•	Értesítések a rendelés állapotáról (elfogadva, készül, kiszállítás alatt) (Véletlenszerű időközönként jönnek a következő állapotok)
•	Becsült szállítási idő megjelenítése
•	Rendelési előzmények megtekintése és kezelése
5. Értékelési és visszajelzési rendszer
•	Éttermek értékelése (csillagok)
•	Értékelések szűrése és rendezése
•	Az éttermek számára visszajelzés
6. Kedvencek és személyre szabás
•	Kedvenc éttermek mentése
•	Személyre szabott ajánlások korábbi rendelések alapján
•	Étkezési preferenciák beállítása (vegetáriánus, gluténmentes, stb.)
7. Étterem kezelőfelület
•	Étterem profil kezelése (menü, árak, nyitvatartás)
•	Rendelések fogadása és kezelése
•	Rendelés státuszának frissítése
•	Étterem elérhetőségének beállítása (online/offline)

Adatbázis struktúra
1. Felhasználói adatok
•	Users (UserID, Username, Email, Password, PhoneNumber)
•	DeliveryAddresses (AddressID, UserID, Address, City, ZipCode)
2. Éttermek és menük
•	Restaurants (RestaurantID, Name, Description, Address, CuisineType, Rating)
•	MenuItems (ItemID, RestaurantID, Name, Description, Price, Category, Image)
•	Categories (CategoryID, Name, Description)
3. Rendelések
•	Orders (OrderID, UserID, OrderDate, TotalAmount, Status)
•	OrderDetails (DetailID, OrderID, ItemID, Quantity, SpecialInstructions)
•	OrderStatus (StatusID, Status, Description)
4. Értékelések
•	Reviews (ReviewID, UserID, RestaurantID, Rating, Comment, Date)
•	MenuItemRatings (RatingID, UserID, ItemID, Rating, Comment)
