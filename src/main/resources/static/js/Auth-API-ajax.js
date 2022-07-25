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

// $(document).ajaxComplete( function () {
//     if (localStorage.getItem(LOGGED_IN) === "true")
//         resetInterval();
//     else
//         clearInterval(intervalId);
// });

window.onload = function (e) {
    if (localStorage.getItem(LOGGED_IN) === "true") {
        requestTokenRefresh();
        // resetInterval();
    } else {
        $(".authenticated").hide();
        // clearInterval(intervalId);
    }
}

