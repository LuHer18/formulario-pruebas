/**
 * @jest-environment jsdom
 */

document.body.innerHTML = `
  <form id="registerForm"></form>

  <div id="alertBox" class="hidden"></div>
  <div id="usersContainer"></div>

  <input id="fullName" />
  <input id="email" />
  <input id="password" />
  <input id="confirmPassword" />

  <span id="fullNameError"></span>
  <span id="emailError"></span>
  <span id="passwordError"></span>
  <span id="confirmPasswordError"></span>
`;

const {
  getUsers,
  saveUsers,
  showAlert,
  hideAlert,
  clearErrors,
  setError,
  isValidEmail,
  isStrongPassword,
  emailAlreadyExists,
  renderUsers,
  validateForm,
} = require("../app");

beforeEach(() => {
  localStorage.clear();

  document.getElementById("alertBox").className = "hidden";
  document.getElementById("alertBox").textContent = "";

  document.getElementById("usersContainer").innerHTML = "";

  document.getElementById("fullNameError").textContent = "";
  document.getElementById("emailError").textContent = "";
  document.getElementById("passwordError").textContent = "";
  document.getElementById("confirmPasswordError").textContent = "";
});

describe("getUsers", () => {

  test("should return empty array if no users exist", () => {
    expect(getUsers()).toEqual([]);
  });

});

describe("saveUsers", () => {

  test("should save users in localStorage", () => {

    const users = [
      {
        fullName: "Juan",
        email: "juan@test.com",
      },
    ];

    saveUsers(users);

    expect(JSON.parse(localStorage.getItem("users"))).toEqual(users);

  });

});

describe("showAlert", () => {

  test("should show success alert", () => {

    showAlert("Usuario registrado");

    const alertBox = document.getElementById("alertBox");

    expect(alertBox.textContent).toBe("Usuario registrado");
    expect(alertBox.classList.contains("hidden")).toBe(false);
    expect(alertBox.classList.contains("alert-success")).toBe(true);

  });

});

describe("hideAlert", () => {

  test("should hide alert", () => {

    showAlert("Mensaje");
    hideAlert();

    const alertBox = document.getElementById("alertBox");

    expect(alertBox.textContent).toBe("");
    expect(alertBox.classList.contains("hidden")).toBe(true);

  });

});

describe("setError", () => {

  test("should set error message", () => {

    setError("email", "Correo inválido");

    const error = document.getElementById("emailError");
    const input = document.getElementById("email");

    expect(error.textContent).toBe("Correo inválido");
    expect(input.classList.contains("input-error")).toBe(true);

  });

});

describe("clearErrors", () => {

  test("should clear all errors", () => {

    setError("email", "Error");
    setError("password", "Error");

    clearErrors();

    expect(document.getElementById("emailError").textContent).toBe("");
    expect(document.getElementById("passwordError").textContent).toBe("");

  });

});

describe("isValidEmail", () => {

  test("should validate correct email", () => {
    expect(isValidEmail("test@test.com")).toBe(true);
  });

  test("should reject invalid email", () => {
    expect(isValidEmail("correo-invalido")).toBe(false);
  });

});

describe("isStrongPassword", () => {

  test("should validate strong password", () => {
    expect(isStrongPassword("Password123")).toBe(true);
  });

  test("should reject weak password", () => {
    expect(isStrongPassword("123")).toBe(false);
  });

});

describe("emailAlreadyExists", () => {

  test("should return true if email exists", () => {

    saveUsers([
      {
        fullName: "Juan",
        email: "juan@test.com",
      },
    ]);

    expect(emailAlreadyExists("juan@test.com")).toBe(true);

  });

  test("should return false if email does not exist", () => {

    saveUsers([
      {
        fullName: "Juan",
        email: "juan@test.com",
      },
    ]);

    expect(emailAlreadyExists("otro@test.com")).toBe(false);

  });

});

describe("renderUsers", () => {

  test("should render empty message", () => {

    renderUsers();

    const container = document.getElementById("usersContainer");

    expect(container.innerHTML).toContain(
      "No hay usuarios registrados todavía."
    );

  });

  test("should render users list", () => {

    saveUsers([
      {
        fullName: "Juan",
        email: "juan@test.com",
      },
    ]);

    renderUsers();

    const container = document.getElementById("usersContainer");

    expect(container.innerHTML).toContain("Juan");
    expect(container.innerHTML).toContain("juan@test.com");

  });

});

describe("validateForm", () => {

  test("should validate correct form", () => {

    const formData = {
      fullName: "Juan Perez",
      email: "juan@test.com",
      password: "Password123",
      confirmPassword: "Password123",
    };

    expect(validateForm(formData)).toBe(true);

  });

  test("should reject empty full name", () => {

    const formData = {
      fullName: "",
      email: "juan@test.com",
      password: "Password123",
      confirmPassword: "Password123",
    };

    expect(validateForm(formData)).toBe(false);

  });

  test("should reject short full name", () => {

    const formData = {
      fullName: "Jo",
      email: "juan@test.com",
      password: "Password123",
      confirmPassword: "Password123",
    };

    expect(validateForm(formData)).toBe(false);

  });

  test("should reject invalid email", () => {

    const formData = {
      fullName: "Juan Perez",
      email: "correo",
      password: "Password123",
      confirmPassword: "Password123",
    };

    expect(validateForm(formData)).toBe(false);

  });

  test("should reject duplicated email", () => {

    saveUsers([
      {
        fullName: "Juan",
        email: "juan@test.com",
      },
    ]);

    const formData = {
      fullName: "Juan Perez",
      email: "juan@test.com",
      password: "Password123",
      confirmPassword: "Password123",
    };

    expect(validateForm(formData)).toBe(false);

  });

  test("should reject weak password", () => {

    const formData = {
      fullName: "Juan Perez",
      email: "juan@test.com",
      password: "123",
      confirmPassword: "123",
    };

    expect(validateForm(formData)).toBe(false);

  });

  test("should reject different passwords", () => {

    const formData = {
      fullName: "Juan Perez",
      email: "juan@test.com",
      password: "Password123",
      confirmPassword: "Password456",
    };

    expect(validateForm(formData)).toBe(false);

  });

});