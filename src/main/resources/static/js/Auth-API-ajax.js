$.ajaxSetup({
    beforeSend: function (xhr, settings) {
        if (settings.type === 'GET' || settings.type === 'POST' || settings.type === 'PUT'
            || settings.type === 'DELETE') {
            if (!(/^http:.*/.test(settings.url) || /^https:.*/
                .test(settings.url))) {
                xhr.setRequestHeader("X-XSRF-TOKEN", Cookies.get('XSRF-TOKEN'));
            }
        }
    }
});

window.onload = function (e) {
    if (localStorage.getItem(LOGGED_IN) === "true") {
        requestTokenRefresh();
    } else {
        $(".authenticated").hide();
    }
}


let baseTime = 600;
let min;
let sec;
const refreshTokenExpiryTime = setInterval(function () {
    min = parseInt(baseTime / 60);
    sec = baseTime % 60;

    $("#expiryTime").html(min + ":" + sec);
    baseTime--;

    if (baseTime < 0) {
        clearInterval(refreshTokenExpiryTime);
        $("#expiryTime").html("Expired");
    }
}, 1000);



