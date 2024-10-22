btnCargar.addEventListener("click", async () => {
  try {
    const resp = await fetch("/api/usuarios");
    if (!resp.ok) {
      const error = await resp.json();
      console.error(error);
      alert(error.message);
      return;
    }

    const usuarios = await resp.json();

    const rows = usuarios.map((usuario) => {
      const rawHtml = `
            <tr>
                <td>${usuario.id}</td>
                <td>${usuario.userName}</td>
                <td>${usuario.name}</td>
                <td>${usuario.email}</td>
                <td>
                    ${usuario.roles.map((rol) => {
                      return `<span>${rol.nombre}</span>`.trim();
                    })}
                </td>
            </tr>
        `;
      const tr = document.createElement("tr");
      tr.innerHTML = rawHtml.trim();
      return tr;
    });

    bodyTbl.append(...rows);
    console.log(usuarios);
  } catch (error) {
    console.error(error);
  }
});
