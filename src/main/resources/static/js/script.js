let currentUser;
const service = "/api/";
let rolesList = []; //Пригодится для отпрвки форм.

// Отправка нового пользователя
async function newUserSender() {
    let checkedRoles = () => {
        let array = []
        let options = document.querySelector('#newUserRoles').options
        for (let i = 0; i < options.length; i++) {
            if (options[i].selected) {
                array.push(rolesList[i])
            }
        }
        return array;
    }
    let user = {
        id: 0,
        name: document.querySelector('#InputName').value,
        surname: document.querySelector('#InputSurname').value,
        age: document.querySelector('#InputAge').value,
        email: document.querySelector('#InputEmail').value,
        password: document.querySelector('#InputPassword').value,
        roles: checkedRoles()
    }
    let head = new Headers();
    head.append('Content-Type', 'application/json;charset=utf-8');
    head.append('Authorization', sessionStorage.getItem("Authorization"));
    const response = await fetch(service + "users",
        {
            method: 'POST',
            headers: head,
            body: JSON.stringify(user)
        });
    if (response.ok) {
        document.getElementById('nav-user_list-tab').click();
        document.querySelector('#InputName').value = "";
        document.querySelector('#InputSurname').value = "";
        document.querySelector('#InputAge').value = "";
        document.querySelector('#InputEmail').value = "";
        document.querySelector('#InputPassword').value = "";
        user = await response.json();
        message('Пользователь ' + user.name + " создан, присвоен ид: " + user.id);
        await fillAllUsersInfoTable();
    } else {
        alert("HTTP-Error: " + response.status);
    }
}

//Заполнение меню редактирования вызванным пользователем
function editButton(fieldId) {
    fieldId = document.querySelector('#' + fieldId)
    document.querySelector('#IdModalEditor').value = fieldId.firstChild.textContent // видимый Ид
    document.querySelector('#RealIdModalEditor').value = fieldId.firstChild.textContent // отправляемый Ид
    fieldId = fieldId.nextElementSibling
    document.querySelector('#InputNameModalEditor').value = fieldId.firstChild.textContent // name
    fieldId = fieldId.nextElementSibling
    document.querySelector('#InputSurnameModalEditor').value = fieldId.firstChild.textContent //surname
    fieldId = fieldId.nextElementSibling
    document.querySelector('#InputAgeModalEditor').value = fieldId.firstChild.textContent // age
    fieldId = fieldId.nextElementSibling
    document.querySelector('#InputEmailModalEditor').value = fieldId.firstChild.textContent //email
    fieldId = fieldId.nextElementSibling
    document.querySelector('#InputPasswordModalEditor').value = "";
    let roles = fieldId.textContent
    let options = document.querySelector('#rolesModalEditor').options
    for (let i = 0; i < options.length; i++) {
        options[i].selected = roles.includes(options[i].textContent);
    }
}

//Отправка отредактированного пользователя
async function editButtonSender() {
    let checkedRoles = () => {
        let array = []
        let options = document.querySelector('#rolesModalEditor').options
        for (let i = 0; i < options.length; i++) {
            if (options[i].selected) {
                array.push(rolesList[i])
            }
        }
        return array;
    }
    let user = {
        id: document.querySelector('#RealIdModalEditor').value,
        name: document.querySelector('#InputNameModalEditor').value,
        surname: document.querySelector('#InputSurnameModalEditor').value,
        age: document.querySelector('#InputAgeModalEditor').value,
        email: document.querySelector('#InputEmailModalEditor').value,
        password: document.querySelector('#InputPasswordModalEditor').value,
        roles: checkedRoles()
    }
    let head = new Headers();
    head.append('Content-Type', 'application/json;charset=utf-8');
    head.append('Authorization', sessionStorage.getItem("Authorization"));
    const response = await fetch(service + "users",
        {
            method: 'PUT',
            headers: head,
            body: JSON.stringify(user)
        });
    document.getElementById('closeModalEditor').click();
    if (response.ok) {
        message('Пользователь ' + user.id + ' отредактирован');
        await fillAllUsersInfoTable();
    } else {
        alert("HTTP-Error: " + response.status);
    }
}

//Заполнение меню удаления вызванным пользователем
function deleteButton(fieldId) {
    fieldId = document.querySelector('#' + fieldId)
    document.querySelector('#IdModalDelete').value = fieldId.firstChild.textContent // id
    fieldId = fieldId.nextElementSibling
    document.querySelector('#InputNameModalDelete').value = fieldId.firstChild.textContent // name
    fieldId = fieldId.nextElementSibling
    document.querySelector('#InputSurnameModalDelete').value = fieldId.firstChild.textContent //surname
    fieldId = fieldId.nextElementSibling
    document.querySelector('#InputAgeModalDelete').value = fieldId.firstChild.textContent // age
    fieldId = fieldId.nextElementSibling
    document.querySelector('#InputEmailModalDelete').value = fieldId.firstChild.textContent //email
    fieldId = fieldId.nextElementSibling
    let roles = fieldId.textContent
    let options = document.querySelector('#rolesModalDelete').options
    for (let i = 0; i < options.length; i++) {
        options[i].selected = roles.includes(options[i].textContent);
    }
}

// Сообщения об операциях
function message(msg) {
    let delay_popup = 1000;
    document.getElementById('ResultMessage').textContent = msg;
    setTimeout("document.getElementById('msg_pop').style.display='block';" +
        "document.getElementById('msg_pop').className += 'fadeIn';", delay_popup);
}

//Отправка команды удаления
async function deleteButtonSender() {
    let head = new Headers();
    head.append('Content-Type', 'text/pain;charset=utf-8');
    head.append('Authorization', sessionStorage.getItem("Authorization"));
    let id = document.getElementById('IdModalDelete').value;
    const response = await fetch(service + "users/" + id,
        {
            method: 'DELETE',
            headers: head
        });
    document.getElementById('closeModalDelete').click();
    if (response.ok) {
        message('Пользователь ' + id + ' удален');
        await fillAllUsersInfoTable();
    } else {
        alert("HTTP-Error: " + response.status);
    }
}

//Возвращает строковае представление списка ролей пользователя
function rolesToString(rolesArray) {
    let roleString = "";
    for (let role of rolesArray) {
        roleString += " " + role.roleTitle.replace("ROLE_", "")
    }
    return roleString;
}

//Возвращает строковое представление одной строки таблицы пользователей
function printTableRow(user) {
    return `<td id="allListId_${user.id}">${user.id}</td><td>${user.name}</td><td>${user.surname}</td>
<td>${user.age ?? ""}</td><td>${user.email}</td><td>${rolesToString(user.roles)}
<td><button type="button" class="btn btn-info text-white" data-bs-toggle="modal"
 data-bs-target="#EditorModal" onClick="editButton('allListId_${user.id}')">Редактировать</button></td>
  <td><button type="button" class="btn btn-danger" data-bs-toggle="modal"
  data-bs-target="#DeleteModal" onClick="deleteButton('allListId_${user.id}')">Удалить.</button></td>`;
}

//Заполнение заголовка и страницы текушего пользователя
function fillCurrentUserInfo(user) {
    let roleString = rolesToString(user.roles);
    document.getElementById('currentUserInfoTable').innerHTML = `
<td>${user.id}</td><td>${user.name}</td><td>${user.surname}</td>
<td>${user.age ?? ""}</td><td>${user.email}</td><td>${roleString}</td>`;
    document.getElementById('headerUserEmailAndRoles').innerHTML = `
<b>${user.email}</b> c правами: ${roleString}`;
}

//Заполнение таблицы всех пользователей
async function fillAllUsersInfoTable() {
    let head = new Headers();
    head.append('Content-Type', 'text/pain;charset=utf-8');
    head.append('Authorization', sessionStorage.getItem("Authorization"));
    const response = await (await fetch(service + "users/", {
        method: 'GET',
        headers: head
    })).json();
    let table = ""
    for (let user of response) {
        table += "<tr>" + printTableRow(user) + "</tr>"
    }
    document.getElementById('AllUsersInfoTable').innerHTML = table
}

//Получить список ролей для заполнения форм
async function rolesInstaller() {
    let head = new Headers();
    head.append('Content-Type', 'text/pain;charset=utf-8');
    head.append('Authorization', sessionStorage.getItem("Authorization"));
    const response = await (await fetch(service + "roles/", {
        method: 'GET',
        headers: head
    })).json();
    for (let i = 0; i < response.length; i++) {
        rolesList[i] = {
            id: response[i].id,
            title: response[i].roleTitle,
            value: response[i].roleTitle.replace('ROLE_', '')
        };
    }
    let selector = "";
    for (let role of rolesList) {
        selector += `<option value="${role.id}">${role.value}</option>`;
    }
    document.getElementById('rolesModalEditor').innerHTML = selector;
    document.getElementById('rolesModalDelete').innerHTML = selector;
    document.getElementById('newUserRoles').innerHTML = selector;
}

// Организует готовую страницу в соответствии с разрешениями пользователя
async function organizePage() {
    rolesInstaller();
    fillCurrentUserInfo(currentUser);
    if (currentUser.roles.some(x => x.roleTitle === "ROLE_ADMIN")) {
        fillAllUsersInfoTable();
        let adminMenu = document.getElementById('v-pills-admin-tab')
        adminMenu.hidden = false;
        adminMenu.click();
    }
}

// Авторизация пользователя
async function credentialButtonSender() {
    let login = document.getElementById('floatingInput').value;
    let pwd = document.getElementById('floatingPassword').value;
    let credentials = 'Basic ' + btoa(login + ":" + pwd);
    let head = new Headers();
    head.append('Content-Type', 'text/pain;charset=utf-8');
    head.append('Authorization', credentials);
    const response = await fetch(service + "userlogin", {
        method: 'GET',
        headers: head
    });
    if (response.ok) {
        sessionStorage.setItem("Authorization", credentials);
        currentUser = await response.json();
        document.getElementById('loginForm').hidden = true;
        document.getElementById('mainPage').hidden = false;
        organizePage();
    } else {
        message('введена неверная пара логин/пароль' + response.statusText)
    }

}











