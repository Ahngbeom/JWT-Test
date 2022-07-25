const requestTokenRefresh = function () {
    let expired;
    $.ajax({
        type: 'GET',
        url: "/api/token/refresh",
        async: false, // 외부 API ajax 요청에 의해서 Token Refresh 요청이 발생할 경우, 비동기 처리 방식이 아닌 동기 방식으로 전환하여 새로운 토큰을 받고 토큰 세팅을 마칠 때까지 요청이 발생한 API ajax 요청을 대기
        success: function (data, status, xhr) {
            accessToken = data.accessToken;
            console.log(accessToken);
            // xhr.setRequestHeader('Authorization', 'Bearer ' + accessToken);
            JsonResult.html();
            localStorage.setItem(LOGGED_IN, "true");
            getUser();
            expired = false;
        },
        error: function (data, status, xhr) {
            console.error(data.responseText);
            JsonResult.html(data.responseText);
            expired = true;
            logout();
        },
        complete: function (data, status, xhr) {
            console.log("Token Refresh:", status);
        }
    });
    return expired;
}

// let baseTime = refreshTokenExpirySeconds;
// let min;
// let sec;
//
// const refreshTokenExpiryInterval = function () {
//     --baseTime;
//     min = parseInt(baseTime / 60);
//     sec = baseTime % 60;
//
//     $("#expiryTime").html(min + ":" + sec);
//
//     if (baseTime < 0) {
//         clearInterval(intervalId);
//         $("#expiryTime").html("Expired");
//     }
// };

// const resetInterval = function () {
//     clearInterval(intervalId);
//     let tokenExpiryTimeDate = tokenExpiryTime('Refresh Token');
//     console.log(tokenExpiryTimeDate);
//     baseTime = tokenExpiryTimeDate - Date.now();
//     console.log(baseTime);
//     intervalId = setInterval(refreshTokenExpiryInterval, 1000);
// }

const tokenExpiryTime = function (...returnToken) {
    $.ajax({
        type: 'GET',
        url: "/api/token/expiry-time",
        dataType: 'JSON',
        beforeSend: function (xhr) {
            xhr.setRequestHeader('Authorization', 'Bearer ' + accessToken);
        },
        success: function (data, status, xhr) {
            // JsonResult.html("<ul>");
            // JsonResult.append(
            //     "<li>Access Token Expiry Time: " +
            //     new Date(data['Access Token']) +
            //     "</li><li>Refresh Token Expiry Time: " +
            //     new Date(data['Refresh Token']) +
            //     "</li></ul>"
            // );

            JsonResult.html(
                "<div className=\"list-group\">" +
                "<div className=\"list-group-item list-group-item-action\" aria-current=\"true\">" +
                " <div className=\"d-flex w-100 justify-content-between\">" +
                "   <h5 className=\"mb-1\">Access Token Expiry Time</h5>" +
                "   </div>" +
                "    <p className=\"mb-1\">" + new Date(data['Access Token']) + "</p>" +
                "       </div>" +
                "    <div className=\"list-group-item list-group-item-action\" aria-current=\"true\">" +
                "      <div className=\"d-flex w-100 justify-content-between\">" +
                "          <h5 className=\"mb-1\">Refresh Token Expiry Time</h5>" +
                "      </div>" +
                "      <p className=\"mb-1\">" + new Date(data['Refresh Token']) + "</p>" +
                "  </div>" +
                "  </div>"
            );
            console.log(data[returnToken]);
            return data[returnToken];
        },
        error: function (data, status, xhr) {
            console.error(data);
        }
    });
}

