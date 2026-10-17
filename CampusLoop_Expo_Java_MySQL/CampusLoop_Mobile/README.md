# CampusLoop Mobile — Expo + Java Spring Boot + MySQL

## Requirements
- VS Code, Node.js 20+, Java JDK 17+, Maven, MySQL Server 8+, MySQL Workbench
- Expo Go installed on Android phone; phone and laptop on the same Wi-Fi

## 1. Database (Workbench)
Open `backend/setup.sql` in MySQL Workbench and execute all statements. Re-run it after app updates: it preserves existing tables and data while creating feature tables and initializing inventory for existing market listings. MySQL Server must remain running; Workbench is only the database manager.

## 2. Java backend (VS Code terminal / PowerShell)
```powershell
cd backend
$env:DB_PASSWORD="YOUR_MYSQL_PASSWORD"
mvn spring-boot:run
```
Leave this terminal running. Backend listens on port 8080. If the Windows firewall asks, allow access on your **private** network only. Do not expose this demo backend to the internet.

## 3. Configure phone-to-laptop address
In a SECOND VS Code terminal run `ipconfig` and find your laptop's Wi-Fi IPv4 address (for example `192.168.1.5`). Change `mobile/config.ts`:
```ts
export const API_BASE_URL = 'http://192.168.1.5:8080';
```
`localhost` does NOT work from a physical phone. Your phone and laptop must be on the same Wi-Fi network.

## 4. Mobile app (VS Code terminal)
```powershell
cd mobile
npm install
npx expo start --lan
```
Open Expo Go on Android and scan the terminal QR code. Create an account and sign in. Listings, requests, chats, notifications, wishlist entries, blogs, campus announcements, and lost-and-found posts are stored in MySQL. Photos and documents are stored under the backend upload directory.

## 5. Check saved data in Workbench
```sql
USE campusloop;
SHOW TABLES;
SELECT * FROM users;
SELECT * FROM posts;
SELECT * FROM deals;
SELECT * FROM messages;
```

## Notes
- This is a functional **hackathon prototype**, not a production service. Paytm payments and payouts are not integrated; the app's `Paid` order status is simulated and is not a real wallet balance or earnings transfer. Live delivery location sharing is optional, foreground-only, and available only during an active delivery while the volunteer keeps the screen open. Identity verification and push notifications are not implemented.
- Marketplace listings support free items and quantities. Accepting an offer decrements stock and automatically declines other pending offers for that listing.
- Students can publish campus blogs and campus announcements, manage lost-and-found posts, save listings to a wishlist, and attach up to five image/document files per listing.
- Completed orders support star ratings and written reviews. Students can file confidential safety reports and open order disputes; administrators can review reports and resume or cancel disputed orders.
- Buyers can also rate products separately from sellers after a completed marketplace order.
- Only `@kongu.edu` email addresses can register or sign in.
- Notifications open their linked deal, listing, chat, support requests, or admin panel.
- The profile links to an admin panel, community rules, in-app support, and the light/dark appearance setting. The admin panel includes aggregate analytics. My listings are grouped into sold, unsold, and other active listings.
- Pickup/drop-off forms use the fixed campus location menu; custom typed locations are not available.
- Java backend uses BCrypt for passwords and bearer tokens saved in `mobile_tokens`. This demo uses HTTP on your private LAN. Production requires HTTPS, hardened security, expiry/revocation policies, validation and automated tests.
- Expo Go tests a real native mobile UI, not a browser page. A standalone APK requires an Expo EAS build later.
- `npm install` and Maven dependency download need internet. Dependencies and app have not been run end-to-end in this environment.
- If Expo complains about version alignment, run `npx expo install --fix` from `mobile`.
- Re-run `backend/setup.sql` in Workbench after updates; it creates the lost-and-found, announcements, and active delivery-location tables without deleting existing data.
