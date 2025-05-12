Az ételrendelő appunk összeköti az éhes felhasználókat a környékbeli éttermekkel. Böngészhetsz különböző éttermek kínálatában, leadhatod a rendelésed, és követheted annak állapotát. #SpeedySpoon

1. Felhasználói regisztráció, profilkezelés
•	Egyszerű regisztrációs folyamat (email, jelszó, név)
•	Profil testreszabása (szállítási cím modosítás)
•	Felhasználói adatok kezelése és módosítása
2. Éttermek és ételek böngészése
•	Éttermek listázása kategóriák szerint (konyha típusa)
•	Keresési és szűrési lehetőségek 
•	Étterem profilok megtekintése (menü, árak)
•	Étel kategóriák szerinti rendezés a könnyebb keresés érdekében
3. Rendelési folyamat
•	Egyszerű kosár kezelés (hozzáadás, eltávolítás, mennyiség módosítása)
•	Gyors újrarendelés korábbi rendelésekből
6. Kedvencek és személyre szabás
•	Személyre szabott ajánlások korábbi rendelések alapján

Adatbázis struktúra
1. Felhasználói adatok
• users (userID, username, email, password, phoneNumber, [address], [city], [orders])
• deliveryAddresses (addressID, userID, address, city, zipCode)
2. Éttermek és menük
• restaurants (restaurantID, name, description, address, cuisineType, rating, minimum_order, opening_hours, restaurant_image, menuItems)
• menuItems (itemID, restaurantID, name, description, price, category, image)
• categories (categoryID, name, description)
3. Rendelések
• orders (orderID, userID, orderDate, totalAmount, status, orderDetails)
• orderDetails (detailID, orderID, itemID, price, quantity, specialInstructions)
• orderStatus (statusID, status, description)
4. Értékelések
• reviews (reviewID, userID, restaurantID, rating, comment, date)
• menuItemRatings (ratingID, userID, itemID, rating, comment)