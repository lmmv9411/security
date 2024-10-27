let state = {};

btnLoad.addEventListener('click', async () => {
    try {
        const resp = await fetch('/api/products');
        const data = await resp.json();

        if (!resp.ok) {
            const err = await resp.json();
            console.error(err);
            return;
        }

        if (data.content.lenght == 0) {
            return;
        }

        state = { data, ...state };

        const product = data.content[0];

        Object.keys(product).forEach(k => {
            const th = document.createElement('th');
            th.innerHTML = `<th>${k}</th>`;
            tblHead.appendChild(th);
        });

        data.content.map(product => {
            const sTr = `<tr>
                <td>${product.id}</td>
                <td>${product.description}</td>
                <td>${product.stock}</td> 
                <td>${new Date(product.income).toLocaleString()}</td>
                <td>${new Date(product.lastMove).toLocaleString()}</td>
                <td>
                    ${new Intl
                      .NumberFormat('es-CO',
                        {
                            style: 'currency',
                            currency: 'COP',
                            maximumFractionDigits: 0
                        }
                    )
                    .format(product.price)}
                </td>   
                <td>${product.typeMove}</td>
            </tr>`

            const tr = document.createElement('tr');

            tr.innerHTML = sTr;

            tblBody.appendChild(tr);
        });

    } catch (err) {
        console.error(err);
    }
});