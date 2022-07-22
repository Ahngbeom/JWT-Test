const adminLogin = function () {
    if (accessToken !== undefined)
        JsonResult.html("이미 로그인 상태입니다.");
    else {
        $.ajax({
            type: 'POST',
            url: "/api/authenticate",
            contentType: "application/json; charset=utf-8",
            dataType: 'JSON',
            data: JSON.stringify({
                username: "admin",
                password: "admin"
            }),
            success(data, status, xhr) {
                accessToken = data.accessToken;
                xhr.setRequestHeader('Authorization', 'Bearer ' + accessToken);
                localStorage.setItem(LOGGED_IN, "true");
                getUser();
            }
        });
    }
}

const logout = function () {
    $.ajax({
        type: 'GET',
        url: "/api/logout",
        success: function (data, status) {
            console.log("Logout: ", status);
            $("#user").html('');
            $(".unauthenticated").show();
            $(".authenticated").hide();
            localStorage.setItem(LOGGED_IN, "false");
            accessToken = undefined;
        }
    });
};

const KakaoLogin = function () {
    // const xhr = new XMLHttpRequest();
    // const url = 'http://localhost:8080/oauth2/authorization/kakao';
    //
    // if (xhr) {
    //     xhr.open('GET', url, true);
    //     xhr.withCredentials = true;
    //     xhr.setRequestHeader('Access-Control-Allow-Origin', '*');
    //     if(xhr.readyState === XMLHttpRequest.DONE) {
    //         const status = xhr.status;
    //         console.log(status);
    //         if (status === 0 || (status >= 200 && status < 400)) {
    //             // The request has been completed successfully
    //             console.log(xhr.responseText);
    //         } else {
    //             // Oh no! There has been an error with the request!
    //         }
    //     }
    //     xhr.send();
    // }
    $.ajax({
        type: 'GET',
        url: 'http://localhost:8080/oauth2/authorization/kakao',
        crossDomain: true,
        dataType: "jsonp",
        format: "json",
        error: function (data) {
            if (data === null || data === "") {
                console.log("Not Logged in");
            }
        },
        success: function (data) {
            console.log(data);
            // $("#user").html(data.nickname);
            // $("#avatar").attr("src", data.picture);
            // $(".unauthenticated").hide();
            // $(".authenticated").show();
        }
    });
}