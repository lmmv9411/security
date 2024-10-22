document.getElementById("frmLogin").addEventListener("submit", async (e) => {
  e.preventDefault();
  e.stopImmediatePropagation();
  const frmLogin = e.target;
  const userName = frmLogin.elements["username"].value;
  const password = frmLogin.elements["password"].value;
  try {
    const resp = await fetch("/api/auth/login", {
      method: "POST",
      body: JSON.stringify({ userName, password }),
      headers: { "Content-Type": "application/json" }
    });
    if (!resp.ok) {
      const error = await resp.json();
      console.log(error);

      alert(error.message);
    } else {
      location.href = "/";
    }
  } catch (error) {
    console.error(error);
  }
});
