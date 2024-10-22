frmCreateUser.addEventListener('submit', async e => {
    e.preventDefault();
    e.stopPropagation();

    const name = frmCreateUser.elements['name'].value;
    const userName = frmCreateUser.elements['username'].value;
    const password = frmCreateUser.elements['password'].value;
    const email = frmCreateUser.elements['email'].value;
    const sRol = rol.value;

    try {
        const resp = await fetch('/api/usuarios', {
            method: 'POST',
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ name, email, userName, password, roles: [{ id: sRol }] })
        });
        if (resp.ok) {
            alert('Creado!')
        } else {
            const error = await resp.json();
            alert('error: ' + error.message)
        }
    } catch (error) {
        console.error(error);

    }

})