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
                // intervalId = setInterval(refreshTokenExpiryInterval, 1000);
            }
        });
    }
}

const userLoginFormModal = function () {
    signupModalTag.querySelectorAll(".visually-hidden").forEach(value => {
        value.classList.remove('visually-hidden');
    });
    signupModalTag.querySelector("#oauth2-links").classList.add('visually-hidden');

    signupModalTag.querySelector(".modal-title").innerHTML = "Sign in";
    signupModalTag.querySelector("#username-available-btn") ?
        signupModalTag.querySelector("#username-available-btn").classList.add('visually-hidden') : undefined;
    signupModalTag.querySelector("#sign-up-nickname") ?
        signupModalTag.querySelector("#sign-up-nickname").parentElement.classList.add('visually-hidden') : undefined;
    signupModalTag.querySelectorAll(".form-check").forEach(value => {
        value.classList.add('visually-hidden');
    });
    signupModalTag.querySelector("#modal-submit").innerHTML = "Login";
    signupModalTag.querySelector("#modal-submit").setAttribute('onclick', 'userLogin()');

    usernameInput.off('input');
    passwordInput.off('input');

    signupModal.show();
}

const userLogin = function () {
    signupModal.hide();
    $.ajax({
        type: 'POST',
        url: '/api/authenticate',
        contentType: "application/json; charset=utf-8",
        dataType: 'JSON',
        data: JSON.stringify({
            username: usernameInput.val(),
            password: passwordInput.val()
        }),
        success(data, status, xhr) {
            accessToken = data.accessToken;
            xhr.setRequestHeader('Authorization', 'Bearer ' + accessToken);
            localStorage.setItem(LOGGED_IN, "true");
            getUser();
            alertToastTag.querySelector('.toast-body').innerHTML = "정상적으로 로그인되었습니다.";
            alertToast.show();
            // intervalId = setInterval(refreshTokenExpiryInterval, 1000);
        },
        error: function (data, status, xhr) {
            console.log(status);
            console.log(data);
        },
        complete: function () {
            signupForm.reset();
        }
    });
}

const oauth2Login = function () {
    signupModalTag.querySelector(".modal-title").innerHTML = "OAuth2 Login";
    signupModalTag.querySelector(".modal-body form").classList.add('visually-hidden');
    signupModalTag.querySelector("#oauth2-links").classList.contains('visually-hidden') ?
        signupModalTag.querySelector(".modal-body #oauth2-links").classList.remove('visually-hidden') : undefined;
    signupModalTag.querySelector("#modal-submit").innerHTML = "Login";

    signupModal.show();

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
            // clearInterval(intervalId);
            location.reload();
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