# CampusLoop — Java + MySQL Workbench edition

A Spring Boot 3 Java backend with MySQL persistence and responsive HTML/CSS/JavaScript frontend. Uses Java 17+ and Maven. **MySQL Workbench is a database client**: run `setup.sql` in Workbench, keep **MySQL Server** running, and start the Java application in VS Code.

## Setup on Windows

1. Install JDK 17 or 21, Maven, MySQL Server 8+, MySQL Workbench, and VS Code Java Extension Pack.
2. Open MySQL Workbench, connect to your local MySQL server, open `setup.sql`, click the lightning bolt to execute the script. Refresh schemas; `campusloop` should appear.
3. Open this project folder in VS Code (the folder containing `pom.xml`). Open **Terminal → New Terminal**.
4. In PowerShell, start the backend from the project root with:

   `powershell -ExecutionPolicy Bypass -File .\backend\run.ps1`

   Enter the same password that works in MySQL Workbench (not your Workbench app password) when prompted. If you use another MySQL username, set `$env:DB_USER="your_username"` in that terminal before running the script.
5. Maven downloads Spring Boot and MySQL Connector/J on the first run (internet required).
6. Visit **http://localhost:8080**. Register two separate accounts to test offers and messaging.

To stop: `Ctrl+C`. To restart later, run the script again and re-enter the password.

## Workbench: view saved data

When updating an existing installation, re-run `setup.sql` after backend updates. Its `CREATE TABLE IF NOT EXISTS` statements add new tables without deleting existing account, listing, or order data.

```sql
USE campusloop;
SELECT * FROM users;
SELECT * FROM posts ORDER BY id DESC;
SELECT * FROM deals ORDER BY id DESC;
SELECT * FROM messages ORDER BY id DESC;
SELECT * FROM direct_messages ORDER BY id DESC;
```

**Never paste a database password into Java source code or commit it to Git.** The startup script prompts for the password without echoing it and only places it in the backend process environment while the application runs.

## Included features

- Registration/login with BCrypt password hashes and server-side sessions; `@kongu.edu` addresses only
- Marketplace, tasks and pickup/drop-off listings with separate pickup and destination locations
- Free listings, available quantity, quantity-aware requests, and saved-for-later listings
- Stock-safe offer acceptance: stock is deducted on acceptance and competing pending offers are declined automatically
- Campus blog feed and publishing
- Offer/request and order status tracking
- Deal chat and private student-to-student chat persisted in MySQL
- Registered-student directory, activity/orders and notifications linked to their event
- Listing photo and document attachments (up to five files, 10 MB each)
- Seller ratings and product ratings on completed orders, shown on student profiles and product listings
- In-app community rules and support tickets, with an administrator support queue
- Confidential safety reports and order disputes, with administrator review and resume/cancel resolution
- Campus pickup and drop-off location choices, including custom locations
- Listing reports and administrator review
- Responsive browser UI; simulated payment completion only

## Demo limitations / known gaps

- Workbench does not run the web server; Java Spring Boot does.
- Campus IDs are **not independently verified**.
- Admins can open the Admin panel from Profile. It includes listing and safety reports, order disputes, and support requests.
- Re-run `setup.sql` after updates; product ratings, notification event targets, and support tickets use new tables.
- `Paid` is only a **demo status**, not UPI/escrow/real payment.
- Handover OTP is displayed to the owner, but verification is not implemented.
- No live GPS, email/SMS, live push notifications, or production deployment.
- Security hardening needed before public deployment: enable CSRF protection, add login rate limits, transactional/concurrency-safe deal transitions, input size checks, permission auditing, secure session cookie over HTTPS, and automated tests.
- To make an admin account for local demo, register with an `@kongu.edu` address first, then execute in Workbench: `UPDATE users SET is_admin=TRUE WHERE email='yourname@kongu.edu';` Log out and back in.
