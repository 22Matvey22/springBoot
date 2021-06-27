$(async function () {
    await getTableWithUsers();
    // getNewUserForm();
    getDefaultModal();
    // addNewUser();
})

const userFetchService = {
    head: {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
    },
    // bodyAdd : async function(user) {return {'method': 'POST', 'headers': this.head, 'body': user}},
    findAllUsers: async () => await fetch('admin/users'),
    findOneUser: async (id) => await fetch(`admin/users/${id}`),
    // addNewUser: async (user) => await fetch('api/users', {method: 'POST', headers: userFetchService.head, body: JSON.stringify(user)}),
    updateUser: async (user, id) => await fetch(`admin/users/${id}`, {method: 'PUT', headers: userFetchService.head, body: JSON.stringify(user)}),
    deleteUser: async (id) => await fetch(`admin/users/${id}`, {method: 'DELETE', headers: userFetchService.head})
}

async function getTableWithUsers() {
    let table = $('#tableAllUsers tbody');
    table.empty();

    await userFetchService.findAllUsers()
        .then(res => res.json())
        .then(users => {
            users.forEach(user => {
                let rolesString = "";
                user.roles.forEach(role => rolesString += role.role + " ");
                let tableFilling = `$(
                        <tr>
                            <td>${user.id}</td>
                            <td>${user.firstName}</td>
                            <td>${user.lastName}</td>
                            <td>${user.age}</td>
                            <td>${user.email}</td>
                            <td class="d-flex">
                                <div>${rolesString}</div>
                            </td>     
                            <td>
                                <button type="button" data-userid="${user.id}" data-action="edit" class="btn btn-sm btn-primary" 
                                data-toggle="modal" data-target="#modalUser">Edit</button>
                            </td>
                            <td>
                                <button type="button" data-userid="${user.id}" data-action="delete" class="btn btn-sm btn-danger" 
                                data-toggle="modal" data-target="#modalUser">Delete</button>
                            </td>
                        </tr>
                )`;
                table.append(tableFilling);
            })
        })
// обрабатываем нажатие на любую из кнопок edit или delete
    // достаем из нее данные и отдаем модалке, которую к тому же открываем
    $("#usersTable").find('button').on('click', (event) => {
        let defaultModal = $('#modalUser');

        let targetButton = $(event.target);
        let buttonUserId = targetButton.attr('data-userid');
        let buttonAction = targetButton.attr('data-action');

        defaultModal.attr('data-userid', buttonUserId);
        defaultModal.attr('data-action', buttonAction);
        defaultModal.modal('show');
    })
}

// что то деалем при открытии модалки и при закрытии
// основываясь на ее дата атрибутах
async function getDefaultModal() {
    $('#modalUser').modal({
        keyboard: true,
        show: false
    }).on("show.bs.modal", (event) => {
        let thisModal = $(event.target);
        let userid = thisModal.attr('data-userid');
        let action = thisModal.attr('data-action');
        switch (action) {
            case 'edit':
                editUser(thisModal, userid);
                break;
            case 'delete':
                deleteUser(thisModal, userid);
                break;
        }
    }).on("hidden.bs.modal", (e) => {
        let thisModal = $(e.target);
        thisModal.find('.modal-title').html('');
        thisModal.find('.modal-body').html('');
        thisModal.find('.modal-footer').html('');
    })
}

// редактируем юзера из модалки редактирования, забираем данные, отправляем
async function editUser(modal, id) {
    let preuser = await userFetchService.findOneUser(id);
    let user = preuser.json();

    modal.find('.modal-title').html('Edit user');

    let closeButton = `<button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>`
    let editButton = `<button  class="btn btn-primary" id="editButton">Edit</button>`;

    modal.find('.modal-footer').append(closeButton);
    modal.find('.modal-footer').append(editButton);

    user.then(user => {
        let bodyForm = `
            <form class="form-group" id="editUser">
                <div class="d-flex flex-column px-5">
                    <div class="mb-3">
                        <label for="modal-id"
                               class="col-form-label">ID</label>
                        <input type="text" name="id"
                               class="form-control"
                               id="modal-id" value="${user.id}" readonly>
                    </div>
                    <div class="mb-3">
                        <label for="modal-firstName"
                               class="col-form-label">First
                            name</label>
                        <input type="text" name="firstName"
                               class="form-control"
                               id="modal-firstName"
                               value="${user.firstName}">
                    </div>

                    <div class="mb-3">
                        <label for="modal-lastName"
                               class="col-form-label">Last
                            name</label>
                        <input type="text" name="lastName"
                               class="form-control"
                               id="modal-lastName"
                               value="${user.lastName}">
                    </div>
                    <div class="mb-3">
                        <label for="modal-age"
                               class="col-form-label">Age</label>
                        <input type="text" name="age"
                               class="form-control"
                               id="modal-age"
                               value="${user.age}">
                    </div>
                    <div class="mb-3">
                        <label for="modal-email"
                               class="col-form-label">Email</label>
                        <input type="email" name="email"
                               class="form-control"
                               id="modal-email"
                               value="${user.email}">
                    </div>
                    <div class="mb-3">
                        <label for="modal-password"
                               class="col-form-label">Password</label>
                        <input type="text" name="password"
                               class="form-control"
                               id="modal-password"
                               value="${user.password}">
                    </div>
                    <div class="d-flex flex-column md-3">
                        <label for="modal-rolesId"
                               class="col-form-label">Role</label>
                        <select id="modal-rolesId"
                                name="rolesId">
                            <option value="1" checked>USER
                            </option>
                            <option value="2">ADMIN</option>
                        </select>
                    </div>
                </div>
            </form>
        `;
        modal.find('.modal-body').append(bodyForm);
    })

    $("#editButton").on('click', async () => {
        let id = modal.find("#id").val().trim();
        let firstName = modal.find("#firstName").val().trim();
        let lastName = modal.find("#lastName").val().trim();
        let age = modal.find("#age").val().trim();
        let email = modal.find("#email").val().trim();
        let password = modal.find("#password").val().trim();
        let roles = modal.find("#modal-rolesId").val().trim();

        let data = {
            id: id,
            firstName: firstName,
            lastName: lastName,
            age: age,
            email: email,
            password: password,
            roles: roles
        }
        const response = await userFetchService.updateUser(data, id);

        if (response.ok) {
            getTableWithUsers();
            modal.modal('hide');
        } else {
            let body = await response.json();
            let alert = `<div class="alert alert-danger alert-dismissible fade show col-12" role="alert" id="sharaBaraMessageError">
                            ${body.info}
                            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>`;
            modal.find('.modal-body').prepend(alert);
        }
    })
}


// удаляем юзера из модалки удаления
async function deleteUser(modal, id) {
    let preUserDelete = await userFetchService.findOneUser(id);
    let userDelete = preUserDelete.json();

    modal.find('.modal-title').html('Delete user');


    let closeButtonDelete = `<button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>`
    let DeleteButton = `<button  class="btn btn-danger" id="deleteButton">Delete</button>`;

    modal.find('.modal-footer').append(closeButtonDelete);
    modal.find('.modal-footer').append(DeleteButton);

    userDelete.then(user => {
        let bodyForm = `
            <form class="form-group" id="deleteUser">
                <div class="d-flex flex-column px-5">
                    <div class="mb-3">
                        <label for="modal-id"
                               class="col-form-label">ID</label>
                        <input type="text" name="id"
                               class="form-control"
                               id="modal-id" value="${user.id}" readonly>
                    </div>
                    <div class="mb-3">
                        <label for="modal-firstName"
                               class="col-form-label">First
                            name</label>
                        <input type="text" name="firstName"
                               class="form-control"
                               id="modal-firstName"
                               value="${user.firstName}">
                    </div>

                    <div class="mb-3">
                        <label for="modal-lastName"
                               class="col-form-label">Last
                            name</label>
                        <input type="text" name="lastName"
                               class="form-control"
                               id="modal-lastName"
                               value="${user.lastName}">
                    </div>
                    <div class="mb-3">
                        <label for="modal-age"
                               class="col-form-label">Age</label>
                        <input type="text" name="age"
                               class="form-control"
                               id="modal-age"
                               value="${user.age}">
                    </div>
                    <div class="mb-3">
                        <label for="modal-email"
                               class="col-form-label">Email</label>
                        <input type="email" name="email"
                               class="form-control"
                               id="modal-email"
                               value="${user.email}">
                    </div>
                    <div class="mb-3">
                        <label for="modal-password"
                               class="col-form-label">Password</label>
                        <input type="text" name="password"
                               class="form-control"
                               id="modal-password"
                               value="${user.password}">
                    </div>
                    <div class="d-flex flex-column md-3">
                        <label for="modal-rolesId"
                               class="col-form-label">Role</label>
                        <select id="modal-rolesId"
                                name="rolesId">
                            <option value="1" checked>USER
                            </option>
                            <option value="2">ADMIN</option>
                        </select>
                    </div>
                </div>
            </form>
        `;
        modal.find('.modal-body').append(bodyForm);
    })

    $("#deleteButton").on('click', async () => {
        await userFetchService.deleteUser(id);
        getTableWithUsers();
        $(function () {
            $('#modalUser').modal('toggle');
        });
    })
}
