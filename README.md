# HeatSystem 🌡️🏢

**HeatSystem** is a Java-based heating management system that handles energy consumption, billing, and task management for buildings. The system consists of **three interconnected applications**: **Director**, **Controller**, and **Tenant**, each with its own set of functionalities and access permissions.  

The project uses **SQLite** for data storage and **Java logging** to track actions, errors, and user activity.  

---

## Features 🚀

### 1. Director App 👨‍💼
**Role:** Administrator/Manager of buildings  

**Capabilities:**
- **Authentication 🔐:** Secure login/logout with session tracking
- **Tenant Management 🏠:** Register tenants, view tenant data, assign apartments
- **Controller Management 👷:** Register controllers, assign tasks, monitor task status
- **Task Delegation 📝:** Assign maintenance or meter-reading tasks to controllers with deadlines and building/apartment references
- **Billing & Pricing 💰:** Set energy price per kWh for buildings, calculate bills based on readings, issue bills to tenants
- **Data Access 📊:** View apartments, buildings, controllers, and all task information
- **Logging 🗂️:** Actions like logins, bill creation, and task assignment are logged for monitoring and auditing

---

### 2. Controller App 🛠️
**Role:** Personnel responsible for collecting energy consumption data  

**Capabilities:**
- **Authentication 🔐:** Secure login/logout with session tracking
- **Task Management ✅:** View assigned tasks from Director and mark them as completed
- **Meter Readings 📈:** Insert readings for individual apartments and main building meters
- **Automatic Updates 🔄:** Task statuses are updated in real time when readings are submitted
- **Logging 🗂️:** All actions such as task updates and meter submissions are logged

---

### 3. Tenant App 🏡
**Role:** Building residents  

**Capabilities:**
- **Authentication 🔐:** Secure login/logout with session tracking
- **Billing 💵:** View bills issued for their apartment
- **Payments 🏦:** Mark bills as paid
- **Logging 🗂️:** Bill payments and logins are logged for auditing

---

## Architecture 🏗️

- **Backend:** Java, JDBC, SQLite
- **Database:** `HeatSystemDB` stores all users, apartments, buildings, meter readings, and billing information
- **Logging 🗃️:** Java's `Logger` tracks user actions, errors, and system events
- **Role-based access 🔑:** Each app enforces specific permissions and functionality depending on the role (Director, Controller, Tenant)
