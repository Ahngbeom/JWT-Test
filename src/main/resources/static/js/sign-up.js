const signUpFormModal = function () {
    signupModalTag.querySelectorAll(".visually-hidden").forEach(value => {
        value.classList.remove('visually-hidden');
    });
    signupModalTag.querySelector("#oauth2-links").classList.add('visually-hidden');
    signupModal.show();
}

const usernameDuplicateCheck = function () {
    let username = usernameInput.val();
    if (koreanReg.test(username)) {
        signupToastTag.querySelector('.toast-body').innerHTML = "Exist korean on username.";
        signupToast.show();
        usernameInput.addClass('is-invalid').removeClass('is-valid');
    } else if (username.length < 3) {
        signupToastTag.querySelector('.toast-body').innerHTML = "Username must be at least 3 characters long.";
        signupToast.show();
        usernameInput.addClass('is-invalid').removeClass('is-valid');
    } else if (requestAvailability4Username(username) === false) {
        signupToastTag.querySelector('.toast-body').innerHTML = "This username isn't available.";
        signupToast.show();
        usernameInput.addClass('is-invalid').removeClass('is-valid');
    } else {
        usernameInput.attr('data-available', 'true');
        signupToast.hide();
        usernameInput.addClass('is-valid').removeClass('is-invalid');
    }
}

const signUp = function () {
    if (!usernameInput.hasClass('is-valid') || usernameInput.attr('data-available') === "false") {
        signupToastTag.querySelector('.toast-body').innerHTML = "Username is incorrect or availability needs to be checked.";
        signupToast.show();
        usernameInput.addClass('is-invalid').removeClass('is-valid');
    } else if (passwordInput.val().length < 4) {
        signupToastTag.querySelector('.toast-body').innerHTML = "Password must be at least 3 characters long.";
        signupToast.show();
        passwordInput.addClass('is-invalid').removeClass('is-valid');
    } else if (nicknameInput.val().length < 2) {
        signupToastTag.querySelector('.toast-body').innerHTML = "Nickname must be at least 2 characters long.";
        signupToast.show();
        nicknameInput.addClass('is-invalid').removeClass('is-valid');
    } else {
        signupToast.hide();
        usernameInput.addClass('is-valid').removeClass('is-invalid');
        passwordInput.addClass('is-valid').removeClass('is-invalid');
        nicknameInput.addClass('is-valid').removeClass('is-invalid');

        let authoritiesSet = [];
        authoritiesInput.forEach(auth => {
            if (auth.checked)
                authoritiesSet.push({
                    authorityName: auth.value
                });
        });

        $.ajax({
            type: 'POST',
            url: '/api/signup',
            data: JSON.stringify({
                username: usernameInput.val(),
                password: passwordInput.val(),
                nickname: nicknameInput.val(),
                authorities: authoritiesSet
            }),
            dataType: 'JSON',
            contentType: "application/json; charset=utf-8",
            success: function (data, status, xhr) {
                signupToast.hide();
                signupModal.hide();
                alertToastTag.querySelector('.toast-body').innerHTML = "정상적으로 가입되었습니다.";
                alertToast.show();
            },
            error: function (data, status, xhr) {
                signupToast.hide();
                signupModal.hide();
                alertToastTag.querySelector('.toast-body').innerHTML = data;
                alertToast.show();
            },
            complete: function () {
                signupForm.reset();
            }
        });
    }
}

const requestAvailability4Username = function (username) {
    let available = false;
    $.ajax({
        type: 'GET',
        url: "/api/user/username-availability",
        data: {
            username: username
        },
        async: false,
        success: function (data, status, xhr) {
            available = data;
        }
    });
    return available;
}

signupForm.addEventListener('reset', function (e) {
    usernameInput.removeClass('is-invalid').removeClass('is-valid');
    passwordInput.removeClass('is-invalid').removeClass('is-valid');
    nicknameInput.removeClass('is-invalid').removeClass('is-valid');
});

usernameInput.on('input', function (e) {
    usernameInput.attr('data-available', 'false');
    usernameInput.removeClass('is-invalid').removeClass('is-valid');
});

passwordInput.on('input', function (e) {
    if (e.target.value.length < 4) {
        passwordInput.addClass('is-invalid').removeClass('is-valid');
    } else {
        passwordInput.addClass('is-valid').removeClass('is-invalid');
    }
});

nicknameInput.on('input', function (e) {
    if (e.target.value.length < 2) {
        nicknameInput.addClass('is-invalid').removeClass('is-valid');
    } else {
        nicknameInput.addClass('is-valid').removeClass('is-invalid');
    }
});