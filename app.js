const form = document.getElementById("registerForm");
const alertBox = document.getElementById("alertBox");
const usersContainer = document.getElementById("usersContainer");

const fullNameInput = document.getElementById("fullName");
const emailInput = document.getElementById("email");
const passwordInput = document.getElementById("password");
const confirmPasswordInput = document.getElementById("confirmPassword");

const fields = {
  fullName: {
    input: fullNameInput,
    error: document.getElementById("fullNameError"),
  },
  email: {
    input: emailInput,
    error: document.getElementById("emailError"),
  },
  password: {
    input: passwordInput,
    error: document.getElementById("passwordError"),
  },
  confirmPassword: {
    input: confirmPasswordInput,
    error: document.getElementById("confirmPasswordError"),
  },
};

function getUsers() {
  return JSON.parse(localStorage.getItem("users")) || [];
}

function saveUsers(users) {
  localStorage.setItem("users", JSON.stringify(users));
}

function showAlert(message) {
  alertBox.textContent = message;
  alertBox.classList.remove("hidden");
  alertBox.classList.add("alert-success");
}

function hideAlert() {
  alertBox.textContent = "";
  alertBox.classList.add("hidden");
  alertBox.classList.remove("alert-success");
}

function clearErrors() {
  Object.values(fields).forEach(({ input, error }) => {
    error.textContent = "";
    input.classList.remove("input-error");
  });
}

function setError(fieldName, message) {
  fields[fieldName].error.textContent = message;
  fields[fieldName].input.classList.add("input-error");
}

function isValidEmail(email) {
  const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  return regex.test(email);
}

function isStrongPassword(password) {
  const regex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d).{8,}$/;
  return regex.test(password);
}

function emailAlreadyExists(email) {
  const users = getUsers();
  return users.some((user) => user.email.toLowerCase() === email.toLowerCase());
}

function renderUsers() {
  const users = getUsers();

  if (users.length === 0) {
    usersContainer.innerHTML = `
      <p class="empty-message">No hay usuarios registrados todavía.</p>
    `;
    return;
  }

  usersContainer.innerHTML = users
    .map(
      (user, index) => `
        <article class="user-card">
          <h3>Usuario ${index + 1}</h3>
          <p><strong>Nombre:</strong> ${user.fullName}</p>
          <p><strong>Correo:</strong> ${user.email}</p>
        </article>
      `
    )
    .join("");
}

function validateForm(data) {
  let isValid = true;

  if (!data.fullName) {
    setError("fullName", "El nombre completo es obligatorio.");
    isValid = false;
  } else if (data.fullName.length < 3) {
    setError("fullName", "El nombre debe tener al menos 3 caracteres.");
    isValid = false;
  }

  if (!data.email) {
    setError("email", "El correo electrónico es obligatorio.");
    isValid = false;
  } else if (!isValidEmail(data.email)) {
    setError("email", "Ingresa un correo electrónico válido.");
    isValid = false;
  } else if (emailAlreadyExists(data.email)) {
    setError("email", "Este correo ya está registrado.");
    isValid = false;
  }

  if (!data.password) {
    setError("password", "La contraseña es obligatoria.");
    isValid = false;
  } else if (!isStrongPassword(data.password)) {
    setError(
      "password",
      "Debe tener al menos 8 caracteres, una mayúscula, una minúscula y un número."
    );
    isValid = false;
  }

  if (!data.confirmPassword) {
    setError("confirmPassword", "Debes confirmar la contraseña.");
    isValid = false;
  } else if (data.password !== data.confirmPassword) {
    setError("confirmPassword", "Las contraseñas no coinciden.");
    isValid = false;
  }

  return isValid;
}

form.addEventListener("submit", (event) => {
  event.preventDefault();

  hideAlert();
  clearErrors();

  const formData = {
    fullName: fullNameInput.value.trim(),
    email: emailInput.value.trim(),
    password: passwordInput.value,
    confirmPassword: confirmPasswordInput.value,
  };

  const valid = validateForm(formData);

  if (!valid) {
    return;
  }

  const users = getUsers();

  users.push({
    fullName: formData.fullName,
    email: formData.email,
  });

  saveUsers(users);
  form.reset();
  showAlert("Usuario registrado correctamente.");
  renderUsers();
});

renderUsers();
