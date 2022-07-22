const requestTokenRefresh = function () {
    let expired = false;
    $.ajax({
        type: 'GET',
        url: "/api/token/refresh",
        // beforeSend: function (xhr) {
        //     xhr.setRequestHeader('Authorization', 'Bearer ' + accessToken);
        // },
        success: function (data, status, xhr) {
            accessToken = data.accessToken;
            JsonResult.html();
            getUser();
            expired = false;
        },
        error: function (data, status, xhr) {
            console.error(data.responseText);
            JsonResult.html(data.responseText);
            expired = true;
        },
        complete: function (data, status, xhr) {
            console.log("Token Refresh:", status);
            localStorage.setItem(LOGGED_IN, "false");
        }
    });
    return expired;
}