let token = localStorage.getItem("JWTToken");
if(token != null) {
    token = "Bearer " + token;
}

function filterVouchers() {
    const params ={
        tourType: document.getElementById('tourType').value || null,
        transferType: document.getElementById('transferType').value || null,
        price: document.getElementById('price').value || null,
        hotelType: document.getElementById('hotelType').value || null
    };

    fetch("/api/vouchers/filter", {
        method: 'POST',
        headers: {
            "Content-Type" : "application/json",
            'Authorization': token,
        },
        body : JSON.stringify(params)
    })
        .then(response => response.json())
        .then(vouchers => {
            const tbody = document.getElementById('managerVoucherTableBody');
            tbody.innerHTML = '';
            for(const voucher of vouchers.results) {
                tbody.innerHTML += `
            <tr>
                <td>${voucher.id}</td>
                <td>${voucher.title}</td>
                <td>${voucher.description}</td>
                <td>${voucher.isHot}</td>
                <td>${voucher.status}</td>
                <td><button onclick="markHot('${voucher.id}', ${voucher.isHot})">${i18n.voucherMark}</button></td>
                <td><button onclick="changeStatus('${voucher.id}', '${voucher.status}')">${i18n.voucherStatus}</button></td>
            </tr>
        `;
            }
        });
}


function markHot(voucherId, isHot) {
    fetch(`/api/vouchers/${voucherId}/status`, {
        method: 'PATCH',
        headers: {
            "Content-Type" : "application/json",
            'Authorization': token
        },
        body: JSON.stringify({ isHot: !isHot })
    }).then(response => {
        if(response.ok) {
            filterVouchers();
        }
    })
}

function changeStatus(voucherId, status) {
    if(status === "REGISTERED") {
        status = "PAID";
    } else if(status === "PAID") {
        status = "CANCELED";
    } else {
        status = "REGISTERED";
    }
    fetch(`/api/vouchers/${voucherId}/condition`, {
        method: 'PATCH',
        headers: {
            "Content-Type" : "application/json",
            'Authorization': token
        },
        body: JSON.stringify({ status: status })
    }).then(response => {
        if(response.ok) {
            filterVouchers();
        }
    })
}
