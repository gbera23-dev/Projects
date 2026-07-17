let token = localStorage.getItem("JWTToken");
if(token != null) {
    token = "Bearer " + token;
}
fetch("http://localhost:8080/api/users/getAuthUser",
    {
        method: "GET",
        headers: {
            "Content-Type" : "application/json",
            'Authorization' : token
        }
    }).then(response => response.json())
    .then(data => {
        const userElem = document.getElementById("usern");
        const balanceElem = document.getElementById("userb");
        const phoneNumberElem =  document.getElementById("userp");
        userElem.textContent = userElem.textContent.concat( " " + data.results.username + "!");
        balanceElem.textContent = balanceElem.textContent.concat(": " + data.results.balance);
        phoneNumberElem.textContent = phoneNumberElem.textContent.concat(": " + data.results.phoneNumber);
        const tbody = document.getElementById("ticketTableBody");
        const removeButton = document.getElementById("remove").textContent;
        for(let elem of data.results.vouchers) {
            const row = document.createElement('tr');
            row.innerHTML = `
        <td>${elem.id}</td>
        <td>${elem.title}</td>
        <td>${elem.description}</td>
        <td><button class="removeButton">${removeButton}</button></td>
    `;
            const button = row.querySelector('.removeButton');
            button.addEventListener('click', () => {
                fetch("http://localhost:8080/api/vouchers/"+elem.id,
                    {
                        method: 'DELETE',
                        headers: {
                            'Authorization': token
                        }
                    }
                )
                    .then(response => response.json())
                    .then(data => {
                        setTimeout(() => {
                            window.location.reload();
                        }, 1000);
                    })
                    .catch(error => alert(error));

            })
            tbody.appendChild(row);
        }
    })
    .catch((error) => {
        console.error('Error:', error);
    })

document.getElementById("profileUpdateRequest")
    .addEventListener("submit", function(event) {
        event.preventDefault();

        const formData = {
            id: null,
            username: document.getElementById("username").value || null,
            password: document.getElementById("password").value || null,
            role: null,
            vouchers: null,
            phoneNumber: document.getElementById("phoneNumber").value || null,
            balance: document.getElementById("balance").value || null,
            active: null
        };

        fetch("http://localhost:8080/api/users/update",
            {
                method: "PATCH",
                headers: {
                    "Content-Type" : "application/json",
                    'Authorization' : token
                },
                body : JSON.stringify(formData)
            }).then(response => {
                if(response.status === 400) {
                    return response.json().then(json => ({isError: true, data: json }));
                } else {
                    return response.json().then(json => ({isError: false, data: json }));
                }
            }
        )
            .then(val => {
                if(!val.isError) {
                    window.confirm("Update successful! refresh the page");
                    window.location.href = "http://localhost:8080/auth/sign-in";
                } else {
                    window.confirm(val.data.validationMessage);
                }
            })
            .catch((error) => {
                console.error('Error:', error);
            })});
