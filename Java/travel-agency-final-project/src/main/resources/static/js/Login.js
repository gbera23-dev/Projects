document.getElementById("logRequest")
    .addEventListener("submit", function(event) {
        event.preventDefault();

        const formData = {
            username: document.getElementById("username").value,
            password: document.getElementById("password").value,
        };
        fetch("http://localhost:8080/api/auth/login",
            {
                method: "POST",
                headers: {
                    "Content-Type" : "application/json"
                },
                body : JSON.stringify(formData)
            }).then(response => {
            if(response.status === 403 || response.status === 500 || response.status === 400) {
                return response.text().then(text => ({isError: true, data: text}));
            }
            else {
                return response.json().then(json => ({isError: false, data: json}));
            }
        })
            .then(val => {
                if(val.isError) {
                    alert("INVALID USERNAME OR PASSWORD!");
                    this.reset();
                } else {
                    window.localStorage.setItem("JWTToken", val.data.generatedJWTToken);
                    document.cookie = `JWTToken=${encodeURIComponent(val.data.generatedJWTToken)}; path=/`;
                    setTimeout(() => {
                        window.location.href = "http://localhost:8080/user/mainPage"
                    }, 1000);
                }
            })
            .catch((error) => {
                console.error('Error:', error);
            })
    });
