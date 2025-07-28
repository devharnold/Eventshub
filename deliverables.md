# Deliverables: Java App - Events Module Refactor (JDBC-Based)

## 🎯 Goal
Refactor the `Events` module for better structure and add support for advanced event queries using plain JDBC.

---

## ✅ Completed
- [ ] Initial project structure
- [ ] Basic Events CRUD with JDBC
- [ ] Refactor `EventsRepository` to `DAO`
- [ ] Added Find Events By Location, used it together with by Date: `findByDateAndLocation`
- [ ] Added Pagination to list Events
- 

---

## 🔧 Refactor Tasks
- [ ] Refactor `Repository` to `DAO` since we want to achieve a classical Java Style
- [ ] Refactor `EventsService` to reduce tight coupling
- [ ] Move SQL queries to constants or external resource files

---

## 🚀 New Features to Add
- [ ] `findEventsByLocation(String location)`
    - [ ] Add SQL query to search by location
    - [ ] Add method in `EventsDAO` for this
    - [ ] Add method in `EventsService`
    - [ ] Integrate with user interface or input handling
- [ ] `findEventsByDateRange(Date startDate, Date endDate)`
- [ ] `findUpcomingEvents()`
- [ ] Add pagination to `listEvents(int page, int size)`
- [ ] Sorting support by name, date, or location

---

## 🧪 Testing
- [ ] Write basic test class for `DAO` using test DB or mocks
- [ ] Manual test scenarios for all new methods
- [ ] Test edge cases (e.g., null location, empty results)

---

## 📦 Deployment
- [ ] Ensure database schema is up-to-date
- [ ] Package the app using `jar` or a build tool like Maven/Gradle
- [ ] Include README and instructions for DB config

---

## 📝 Notes
- Optionally, build a simple CLI menu for interaction/testing
- Keep SQL injection prevention in mind (use `PreparedStatement`)
