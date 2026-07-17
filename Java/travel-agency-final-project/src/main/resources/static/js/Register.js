document.getElementById("regRequest")
    .addEventListener("submit", function(event) {
        event.preventDefault();

        const formData = {
            username: document.getElementById("username").value,
            password: document.getElementById("password").value,
            repeatedPassword: document.getElementById("repeatedPassword").value,
            role: "ROLE_USER"
        };
        if(formData.password !== formData.repeatedPassword) {
            alert("Passwords do not match! Please, confirm password correctly!");
        } else {
            fetch("http://localhost:8080/api/auth/register",
                {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json"
                    },
                    body: JSON.stringify(formData)
                }).then(response => {
                    if(response.status === 400) {
                        return response.json().then(json => ({isError: true, data: json }));
                    } else {
                        return response.json().then(json => ({ isError: false, data: json }));
                    }
                }
            )
                .then(val => {
                    if(!val.isError) {
                        window.localStorage.setItem("JWTToken", val.data.generatedJWTToken);
                        document.cookie = `JWTToken=${encodeURIComponent(val.data.generatedJWTToken)}; path=/`;
                        window.confirm("Registration successful! redirecting to main page...");
                        setTimeout(() => {
                            window.location.href = "http://localhost:8080/user/mainPage"
                        }, 500);
                    } else {
                        window.confirm(val.data.validationMessage)
                    }
                })
                .catch((error) => {
                    console.error('Error:', error);
                })
        }
    });

