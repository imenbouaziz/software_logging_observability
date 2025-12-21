/* =====================================================
   OpenTelemetry – Automatic User Scenarios (1 → 8)
   Runs inside the React application
===================================================== */

const API = 'http://localhost:8080';
const wait = (ms) => new Promise(res => setTimeout(res, ms));

/* ======================
   COMMON HELPERS
====================== */

async function register(email, password) {
  return fetch(`${API}/users/register`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ email, password })
  });
}

async function login(email, password) {
  return fetch(`${API}/users/login`, {
    method: 'POST',
    credentials: 'include',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ email, password })
  });
}

async function logout() {
  return fetch(`${API}/users/logout`, {
    method: 'POST',
    credentials: 'include'
  });
}

async function getProducts(userId) {
  return fetch(`${API}/users/${userId}/products`, {
    credentials: 'include'
  });
}

async function getProductById(userId, id) {
  return fetch(`${API}/users/${userId}/products/${id}`, {
    credentials: 'include'
  });
}

async function getProductByNameGlobal(userId, name) {
  return fetch(`${API}/users/${userId}/products/global/name/${name}`, {
    credentials: 'include'
  });
}

async function getProductsGlobal(userId) {
  return fetch(`${API}/users/${userId}/products/global`, {
    credentials: 'include'
  });
}

async function addProduct(userId, product) {
  return fetch(`${API}/users/${userId}/products`, {
    method: 'POST',
    credentials: 'include',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(product)
  });
}

async function updateProduct(userId, id, name, price, expirationDate) {
  return fetch(
    `${API}/users/${userId}/products/${id}?name=${encodeURIComponent(name)}&price=${price}&expiration_date=${expirationDate}`,
    {
      method: 'PUT',
      credentials: 'include'
    }
  );
}

async function deleteProduct(userId, id) {
  return fetch(`${API}/users/${userId}/products/${id}`, {
    method: 'DELETE',
    credentials: 'include'
  });
}

/* ======================
   SCENARIO 1 – New user
====================== */
export async function scenario1_NewUser() {
  console.log('▶ Scenario 1 – New User');

  await register('userA@test.com', 'password');
  await wait(300);

  await login('userA@test.com', 'password');
  await wait(300);

  await getProducts(1);
  await wait(300);

  await logout();
}

/* ======================
   SCENARIO 2 – Session user
====================== */
export async function scenario2_SessionUser() {
  console.log('▶ Scenario 2 – Existing Session');

  await getProducts(1);
  await wait(200);

  await getProductById(1, 1);
  await wait(200);

  await getProductByNameGlobal(1, 'Milk');
  await logout();
}

/* ======================
   SCENARIO 3 – Expensive products
====================== */
export async function scenario3_ExpensiveProducts() {
  console.log('▶ Scenario 3 – Expensive Products');

  await login('userC@test.com', 'password');
  await wait(300);

  await getProducts(1);
  for (let i = 1; i <= 5; i++) {
    await getProductById(1, i);
    await wait(150);
  }

  await getProductsGlobal(1);
  await getProductById(1, 1);
}

/* ======================
   SCENARIO 4 – CRUD user
====================== */
export async function scenario4_CRUDUser() {
  console.log('▶ Scenario 4 – CRUD User');

  await login('userD@test.com', 'password');

  await addProduct(1, { name: 'Milk', price: 3, expirationDate: '2025-01-01' });
  await addProduct(1, { name: 'Bread', price: 1.2, expirationDate: '2025-01-02' });
  await addProduct(1, { name: 'Cheese', price: 5, expirationDate: '2025-01-10' });

  await updateProduct(1, 1, 'Milk Updated', 3.5, '2025-01-03');
  await updateProduct(1, 2, 'Bread Updated', 1.5, '2025-01-04');

  await deleteProduct(1, 3);
  await getProducts(1);

  await logout();
}

/* ======================
   SCENARIO 5 – Errors
====================== */
export async function scenario5_Errors() {
  console.log('▶ Scenario 5 – Errors');

  await login('userE@test.com', 'password');

  await getProductById(1, 999);       // 404
  await deleteProduct(1, 999);        // 404
  await updateProduct(1, 1, '', -1);  // 400

  await getProductById(1, 1);
  await logout();
}

/* ======================
   SCENARIO 6 – Unauthorized
====================== */
export async function scenario6_Unauthorized() {
  console.log('▶ Scenario 6 – Unauthorized');

  await fetch(`${API}/users/1/products`); // 401
  await wait(200);

  await login('userF@test.com', 'password');
  await getProducts(1);
  await logout();
}

/* ======================
   SCENARIO 7 – Global navigation
====================== */
export async function scenario7_GlobalNavigation() {
  console.log('▶ Scenario 7 – Global Navigation');

  await login('userG@test.com', 'password');

  await getProductsGlobal(1);
  await getProductByNameGlobal(1, 'Milk');
  await getProductById(1, 1);

  await logout();
}

/* ======================
   SCENARIO 8 – Light stress
====================== */
export async function scenario8_StressTest() {
  console.log('▶ Scenario 8 – Light Stress');

  await login('userH@test.com', 'password');

  for (let i = 0; i < 10; i++) {
    await getProductById(1, 1);
  }

  for (let i = 0; i < 5; i++) {
    await updateProduct(1, 1, 'Milk Stress', 3 + i, '2025-01-10');
  }

  await logout();
}

/* ======================
   RUN ALL SCENARIOS
====================== */
export async function runAllScenarios() {
  await scenario1_NewUser();
  await scenario2_SessionUser();
  await scenario3_ExpensiveProducts();
  await scenario4_CRUDUser();
  await scenario5_Errors();
  await scenario6_Unauthorized();
  await scenario7_GlobalNavigation();
  await scenario8_StressTest();

  console.log('✅ All scenarios executed');
}
