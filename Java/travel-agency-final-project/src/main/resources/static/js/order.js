document.getElementById('confirm').addEventListener('click', function(event) {
    event.preventDefault();
    let token = localStorage.getItem("JWTToken");
    let voucherId = document.querySelector('div[field]').getAttribute('field');

    if(token != null) {
        token = "Bearer " + token;
    }
    fetch('/api/vouchers/order/'+voucherId, {
        method: 'POST',
        headers: {
            'Authorization' : token
        },
    })
        .then(response => response.status)
        .then(status => {
            if(status === 400) {
                window.confirm("Insufficient funds...");
            }
            else {
                window.confirm("Voucher has been ordered successfully!");
                setTimeout(() => {
                    window.location.href = "http://localhost:8080/user/mainPage"
                }, 1000);
            }
        })
        .catch(error => alert(error));
});
