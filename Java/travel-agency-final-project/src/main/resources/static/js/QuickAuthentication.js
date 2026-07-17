document.getElementById('Authentication').addEventListener('click', function(event) {
    event.preventDefault();
    let token = localStorage.getItem("JWTToken");
    if(token != null) {
        token = "Bearer " + token;
    } else {
        token = "non";
    }
    fetch('api/auth/validate', {
        method: 'GET',
        headers: {
            'Authorization' : token
        },
    })
        .then(response => response.status)
        .then(data => {
            if (data === 200) {
                setTimeout(() => {
                    window.location.href = "http://localhost:8080/user/mainPage"
                }, 1000);
            } else {
                console.log("User does not have a session, must register or login via username" +
                    "and password");
                setTimeout(() => {
                    window.location.href = "http://localhost:8080/auth/sign-in"
                }, 1000);
            }
        })
        .catch(error => alert(error));
});
