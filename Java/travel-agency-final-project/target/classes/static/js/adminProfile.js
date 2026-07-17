//
// <div sec:authorize="hasRole('ADMIN')">
//     <div class="signOut">
//         <a th:href="@{/auth/sign-out}" class="btn btn-outline-success me-2" th:text="#{profile.logOut}"></a>
//     </div>
//     <div class="container mt-5">
//         <h2>Voucher Management</h2>
//
//         <!-- Add Voucher Form -->
//         <form id="addVoucherForm">
//             <input id="addTitle" placeholder="Title"/>
//             <input id="addDescription" placeholder="Description"/>
//             <input id="addPrice" placeholder="Price"/>
//             <input id="addTourType" placeholder="Tour Type"/>
//             <input id="addTransferType" placeholder="Transfer Type"/>
//             <input id="addHotelType" placeholder="Hotel Type"/>
//             <input id="addStatus" placeholder="Status"/>
//             <button type="button" onclick="submitAddVoucher()">Add Voucher</button>
//         </form>
//
//         <!-- Edit Voucher Form (hidden until Edit clicked) -->
//         <div id="editVoucherSection" style="display:none;">
//             <input id="editVoucherId" type="hidden"/>
//             <input id="editTitle" placeholder="Title"/>
//             <input id="editDescription" placeholder="Description"/>
//             <input id="editIsHot" placeholder="Hot (true/false)"/>
//             <input id="editStatus" placeholder="Status"/>
//             <button type="button" onclick="submitEditVoucher()">Save</button>
//             <button type="button" onclick="document.getElementById('editVoucherSection').style.display='none'">Cancel</button>
//         </div>
//
//         <table class="table table-bordered">
//             <thead>
//             <tr>
//                 <th>ID</th><th>Title</th><th>Description</th>
//                 <th>Hot</th><th>Status</th><th>Actions</th>
//             </tr>
//             </thead>
//             <tbody id="adminVoucherTableBody"></tbody>
//         </table>
//     </div>
//
//     <div class="container mt-5">
//         <h2>User Management</h2>
//         <table class="table table-bordered">
//             <thead>
//             <tr>
//                 <th>ID</th><th>Username</th><th>Role</th><th>Status</th><th>Actions</th>
//             </tr>
//             </thead>
//             <tbody id="adminUserTableBody"></tbody>
//         </table>
//     </div>
//
//     <script th:src="@{/js/adminProfile.js}"></script>
// </div>



<!-- Add Voucher Form -->
//         <form id="addVoucherForm">
//             <input id="addTitle" placeholder="Title"/>
//             <input id="addDescription" placeholder="Description"/>
//             <input id="addPrice" placeholder="Price"/>
//             <input id="addTourType" placeholder="Tour Type"/>
//             <input id="addTransferType" placeholder="Transfer Type"/>
//             <input id="addHotelType" placeholder="Hotel Type"/>
//             <input id="addStatus" placeholder="Status"/>
//             <button type="button" onclick="submitAddVoucher()">Add Voucher</button>
//         </form>

// private String id;
//
// private String title;
//
// private String description;
//
// private Double price;
//
// private String tourType;
//
// private String transferType;
//
// private String hotelType;
//
// private String status;
//
// private LocalDate arrivalDate;
//
// private LocalDate evictionDate;
//
// private UUID userId;
//
// private Boolean isHot;

let token = localStorage.getItem("JWTToken");
if(token != null) {
    token = "Bearer " + token;
}


function filterVouchers() {
    const params ={
        tourType: document.getElementById('tourType1').value || null,
        transferType: document.getElementById('transferType1').value || null,
        price: document.getElementById('price1').value || null,
        hotelType: document.getElementById('hotelType1').value || null
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
            const tbody = document.getElementById('adminVoucherTableBody');
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
                <td><button onclick="goToVoucherEditPage('${voucher.id}')">${i18n.voucherEdit}</button></td>
            </tr>
        `;
            }
        });
}


function submitAddVoucher() {
    const form = {
        id : crypto.randomUUID(),
        title : document.getElementById("addTitle").value || null,
        description : document.getElementById("addDescription").value || null,
        price : document.getElementById("addPrice").value || null,
        tourType : document.getElementById("addTourType").value || null,
        transferType: document.getElementById("addTransferType").value || null,
        hotelType : document.getElementById("addHotelType").value || null,
        status : document.getElementById("addStatus").value || null,
        arrivalDate : document.getElementById("addArrivalDate").value || null,
        evictionDate : document.getElementById("addEvictionDate").value || null,
        isHot : (document.getElementById("addIsHot").value === "on") || null
    }

    fetch(`/api/vouchers`, {
        method: 'POST',
        headers: {
            "Content-Type" : "application/json",
            'Authorization': token
        },
        body: JSON.stringify(form)
    }).then(response => response.json())
        .then(data => {
            console.log(JSON.stringify(data));

            }
        )
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

function searchUser() {
    const username = document.getElementById('searchUsername').value;
    fetch(`/api/users?username=${username}`, {
        headers: { 'Authorization': token }
    })
        .then(r => r.json())
        .then(u => {
            console.log(JSON.stringify(u));
            const tbody = document.getElementById('adminUserTableBody');
            tbody.innerHTML = `
            <tr>
                <td>${u.results.id}</td>
                <td>${u.results.username}</td>
                <td>${u.results.role}</td>
                <td>${u.results.active ? '🔒 Blocked' : '✅ Active'}</td>
                <td>
                    <button onclick="toggleBlock('${u.results.id}', ${u.results.active})">
                        ${u.results.active ? 'Unblock' : 'Block'}
                    </button>
                </td>
            </tr>
        `;
        });
}

function toggleBlock(id, active) {
    if(active) {
        deactivate(id);
    } else {
        activate(id);
    }
}

function activate(id) {
    fetch("/api/users/"+id, {
        method: 'PATCH',
        headers: { 'Authorization': token, 'Content-Type': 'application/json' },
    }).then(r => {
        if (r.ok) searchUser();
    });
}

function deactivate(id) {
    fetch("/api/users/"+id, {
        method: 'DELETE',
        headers: { 'Authorization': token, 'Content-Type': 'application/json' },
    }).then(r => {
        if (r.ok) searchUser();
    });
}

function goToVoucherEditPage(id) {
    window.location.href = "http://localhost:8080/vouchers/edit/" + id;
}

function editVoucher(id) {
    fetch("/api/vouchers/" + id, {
        headers: {'Authorization': token}
    })
        .then(r => r.json())
        .then(current => {
                const form = {
                    id: id,
                    title: document.getElementById("editTitle").value || current.results.title,
                    description: document.getElementById("editDescription").value || current.results.description,
                    price: document.getElementById("editPrice").value || current.results.price,
                    tourType: document.getElementById("editTourType").value || current.results.tourType,
                    transferType: document.getElementById("editTransferType").value || current.results.transferType,
                    hotelType: document.getElementById("editHotelType").value || current.results.hotelType,
                    status: document.getElementById("editStatus").value || current.results.status,
                    arrivalDate: document.getElementById("editArrivalDate").value || current.results.arrivalDate,
                    evictionDate: document.getElementById("editEvictionDate").value || current.results.evictionDate,
                    isHot: document.getElementById("editIsHot").value === "on" || current.results.isHot
                };
                fetch("/api/vouchers/" + id, {
                    method: 'PATCH',
                    headers: {
                        "Content-Type" : "application/json",
                        'Authorization': token
                    },
                    body: JSON.stringify(form)
                })
                    .then(response => response.json)
                    .then(data => {
                            window.location.href = "http://localhost:8080/vouchers/edit/"+id;
                        }
                    )
            }
        )
}




