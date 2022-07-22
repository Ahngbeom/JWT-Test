const getUser = function () {
    $.ajax({
        type: 'GET',
        url: "/api/user",
        beforeSend: function (xhr) {
            xhr.setRequestHeader('Authorization', 'Bearer ' + accessToken);
        },
        success: function (data) {
            if (data === null || data === "") {
                console.log("Not Logged in");
                return;
            }
            $('#user').html(data.username);
            // $("#avatar").attr("src", data.picture);
            $('.unauthenticated').hide();
            $('.authenticated').show();
            return data;
        }
    });
};

const userList = function () {
    $.ajax({
        type: 'GET',
        url: "/api/user/list",
        contentType: "application/json; charset=utf-8",
        dataType: 'JSON',
        beforeSend: function (xhr) {
            if (requestTokenRefresh() === true) {
                xhr.abort();
            } else {
                xhr.setRequestHeader('Authorization', 'Bearer ' + accessToken);
            }
        },
        success: function (data, status, xhr) {
            let ulTag = "<ul class='list-group'>";
            data.forEach(function (element, index) {
                // console.log(element);
                ulTag += "<li class='list-group-item'>userId: " + element.userId + "</li>";
                ulTag += "<li class='list-group-item'>username: " + element.username + "</li>";
                ulTag += "<li class='list-group-item'>password: " + element.password + "</li>";
                ulTag += "<li class='list-group-item'>nickname: " + element.nickname + "</li>";
                ulTag += "<li class='list-group-item'>activated: " + element.activated + "</li>";
                let subUlTag = "<li class='list-group-item'><ul class='list-group'>authorities";
                element.authorities.forEach(function (element, index) {
                    // console.log(element);
                    subUlTag += "<li class='list-group-item'>" + element.authorityName + "</li>";
                });
                subUlTag += "</ul></li>";
                ulTag += subUlTag;
            });
            ulTag += "</ul>";
            JsonResult.html(ulTag);
        },
        error: function (data, status, xhr) {
            logout();
            localStorage.setItem(LOGGED_IN, "false");
        },
        complete: function (data, status, xhr) {
            console.error("Get User List: ", status);
            console.log(JSON.parse(data.responseText));
        }

    });
};