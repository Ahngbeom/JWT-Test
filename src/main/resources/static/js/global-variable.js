// Global variable to store JWT Access Token
let accessToken;

// HTML Tag to write JSON results of all APIs in this application
const JsonResult = $("#JsonResult");

// Key Name for Login check on Local Storage
const LOGGED_IN = "Logged in";

// const refreshTokenExpirySeconds = 600;
// let intervalId;

const koreanReg = /[ㄱ-ㅎ|ㅏ-ㅣ|가-힣]/;

const alertToastTag = document.getElementById('alertToast');
const alertToast = new bootstrap.Toast(alertToastTag);

const signupToastTag = document.getElementById('signupToast');
const signupToast = new bootstrap.Toast(signupToastTag);

const signupModalTag = document.getElementById('signupModal');
const signupModal = new bootstrap.Modal(signupModalTag);

const signupForm = document.getElementById("sign-up-form");
const usernameInput = $("#sign-up-username");
const passwordInput = $("#sign-up-password");
const nicknameInput = $("#sign-up-nickname");
const authoritiesInput = document.querySelectorAll("input[name='authorities']");