# Quick Start Guide

## Overview

This project demonstrates logging instrumentation with Spoon, OpenTelemetry tracing, and Zipkin visualization across a React + Spring Boot stack with Firebase

---

## Part 1: Automated Logging with Spoon

### Prerequisites for Logging

- Java 
- Maven 
- Firebase credentials configured in `backend/src/main/resources/firebase-config.json`

---

### Spoon Injection Process

```
Source Code (Controllers)
         ↓
   Spoon Analyzer
         ↓
   Filter Classes (Controllers only)
         ↓
   Inject Log Statements
         ↓
   Generate Instrumented Code
         ↓
   Run Backend → Generate Logs
         ↓
   Parse Logs → User Profiles
```

---

### Running Spoon Injection

#### Step 1: Run Spoon Instrumentation (Optional)

> **Note:** The code is already instrumented. Only run this if you modify the source code or want to check if it works

**Output Location:** `spoon-injection/target/spooned/`

This generates instrumented Java files with logging statements automatically inserted

#### Step 2: Run the Instrumented Backend

**Backend URL:** http://localhost:8080

---

###  Log Files & Structure

#### Generated Log File

**Location:** `logs_tp_backend_spooned/logs/app.log`

**Format:**
```
ACTION | userId=2 | action=READ | method=fetchProductById | expensiveCount=1 | totalExpensiveProducts=5
ACTION | userId=3 | action=WRITE | method=addProduct | expensiveCount=0 | totalExpensiveProducts=5
ACTION | userId=1 | action=READ | method=fetchAllProducts | expensiveCount=0 | totalExpensiveProducts=5
```

---

### Testing & Generating Logs

#### Option 1: Manual Testing (Browser)

1. Open http://localhost:5173 (frontend)
2. Register and login
3. Perform actions (add/view/update/delete products)
4. Logs are written automatically

#### Option 2: Automated Tests (Postman/Newman)

**Using Postman:**
1. Import: `logs_tp_backend_spooned/src/main/resources/postman-collection.json`
2. Enable cookie persistence
3. Click "Run Collection"
4. Logs generated for 10 test users with diverse scenarios

**Using Newman:**
```bash
cd backend
newman run src/main/resources/postman-collection.json --cookie-jar cookies.json
```

---

### Log Parsing & Analysis

#### Step 1: Parse Raw Logs
run Parser class in the spoon code

**Output Location:** `spoon/profiles/`

**Files Generated:**
```
profiles/
├── userId_1.json
├── userId_2.json
├── userId_3.json
└── ...
```

#### Step 2: View User Profiles

**Example Profile (`user1.json`):**
```json
{
  "userId": "user:1",
  "userType": "READ_ORIENTED",
  "stats": {
    "reads": 12,
    "writes": 0,
    "expensiveProductSearches": 3,
    "totalExpensiveProducts" : 10
  }
}
```



**User Classification:**
- **READ_ORIENTED:** More reads than writes 
- **WRITE_ORIENTED:** More writes than reads

---

### LPS (Log Profile Structure)

#### What is LPS?

LPS is a structured representation of log entries using the Builder Pattern for immutability

#### Generate LPS
run src/org/example/lps/MainLps in the project spoon


**Output:** Console display + lps-results.json

**Example LPS Object:**
```json
{
  "timestamp" : "2025-12-21T21:00:26.214+01:00",
  "event" : "ACTION",
  "userId" : "10",
  "action" : "READ",
  "method" : "fetchProductByNameGlobal",
  "expensiveCount" : "0",
  "totalExpensiveProducts" : "10"
}
```


**Benefits:**
- Immutable final objects
- Type-safe construction
- Easy to extend with new fields

---

### Log Analysis Results

#### Execution Flow Summary
```
1. Run Backend (generates logs)
   ↓
2. Perform actions or run Newman tests
   ↓
3. Logs written to app.log
   ↓
4. Run Parser.java
   ↓
5. User profiles in spoon/profiles/*.json
   ↓
6. Run MainLps.java 
   ↓
7. Structured LPS objects (console/JSON)
```
---

## Part 2: Tracing with OpenTelemetry

###  Prerequisites for Tracing

- Java 
- Maven 
- Node.js 
- Zipkin JAR file
- Backend and Frontend from Part 1

---

### OpenTelemetry Setup

#### What is OpenTelemetry?

OpenTelemetry (OTel) traces requests across services (frontend → backend) to visualize the complete request journey

#### Architecture
```
React Frontend (Port 5173)
    ↓ HTTP Request + traceparent header
Spring Boot Backend (Port 8080)
    ↓ Both send traces
Zipkin (Port 9411) - Visualization
```

---

### Running Tracing Infrastructure

#### Step 1: Start Zipkin first

**Download Zipkin JAR (if needed)

**Run Zipkin:**
```bash
java -jar zipkin.jar
```

**Verify:** Open http://localhost:9411

#### Step 2: Start Backend with OpenTelemetry

**Basic Mode (No Tracing):**
```bash
cd backend
mvn spring-boot:run
```

**With OpenTelemetry Agent:**
```bash
cd backend

# Download OTel agent if not present
wget https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/latest/download/opentelemetry-javaagent.jar

# Run with tracing
java -javaagent:opentelemetry-javaagent.jar \
  -Dotel.service.name=backend \
  -Dotel.traces.exporter=zipkin \
  -Dotel.exporter.zipkin.endpoint=http://localhost:9411/api/v2/spans \
  -Dotel.metrics.exporter=none \
  -Dotel.logs.exporter=none \
  -jar target/logs_tp-1.0-SNAPSHOT.jar
```
if you need to, build the jar before running with tracing: 
```bash
mvn clean package
```

**Backend URL:** http://localhost:8080

#### Step 3: Start Frontend with OpenTelemetry
```bash
cd frontend
npm run dev
```

**Frontend URL:** http://localhost:5173

**Automatic Instrumentation:**
- `DocumentLoadInstrumentation` - Traces page loads
- `FetchInstrumentation` - Traces API calls with `traceparent` header propagation

---

### Viewing Traces in Zipkin

#### Access Zipkin

**URL:** http://localhost:9411

#### Navigation

1. Click **"Run Query"**
2. Filter by:
   - **Service name:** `frontend` or `backend`
   - **Time range:** Last hour, last day, etc.
3. Click on any trace to see detailed timeline


**Span Details:**
- Duration (milliseconds)
- HTTP status code
- Request/response headers
- Custom events from loggers
- Error information (if any)

**End-to-End Traces:**
- Single trace ID spanning both frontend and backend
- Complete request flow visualization
- Parent-child span relationships

---

### Custom Logger Integration

#### Frontend Custom Loggers

**Location:** `frontend/src/utils/logger.js`

**Available Functions:**
```javascript
logInfo(message, data)   // Info-level events
logError(message, error) // Error tracking
logDebug(message, data)  // Debug information
```

**How They Work:**
1. Log to browser console
2. Get active OpenTelemetry span
3. Add event to span with level and metadata


**Result in Zipkin:**
Each span shows attached events:
```
Span: fetch POST /users/login
  Events:
    - [INFO] User attempting login {email: "user@example.com"}
    - [INFO] Login successful {userId: "123"}
```

---



### End-to-End Tracing

#### How It Works

**W3C Trace Context Standard:**
- Frontend generates trace ID
- Adds `traceparent` header to API calls
- Backend receives header and continues trace
- Both services report to Zipkin with same trace ID


**Benefits:**
- Complete request visibility
- Identify bottlenecks (frontend vs backend)
- Track errors across services
- Measure end-to-end latency


---

### Complete Execution Flow

#### Development Flow
```
1. Start Zipkin (java -jar zipkin.jar)
   ↓
2. Start Backend with OTel agent
   ↓
3. Start Frontend (npm run dev)
   ↓
4. Interact with UI at http://localhost:5173
   ↓
5. View traces at http://localhost:9411
```

#### Full Observability Stack
```
Logging (Part 1):
  → Run backend → Generate logs
  → Parse logs → User profiles
  → Analyze behavior patterns

Tracing (Part 2):
  → Run Zipkin + Backend + Frontend
  → Perform actions
  → View request flows in Zipkin
  → Identify performance issues
```

---

### Configuration Files

#### Frontend OpenTelemetry Config

**File:** `frontend/src/config/OpenTelemetry.js`

#### Backend OpenTelemetry Config

**Via JVM Arguments:**
```bash
-Dotel.service.name=backend
-Dotel.traces.exporter=zipkin
-Dotel.exporter.zipkin.endpoint=http://localhost:9411/api/v2/spans
```

---

## Key URLs

| Service | URL | Purpose |
|---------|-----|---------|
| Frontend | http://localhost:5173 | React application |
| Backend | http://localhost:8080 | REST API |
| Zipkin | http://localhost:9411 | Trace visualization |
| Firebase Console | https://console.firebase.google.com | Database viewer |

---
