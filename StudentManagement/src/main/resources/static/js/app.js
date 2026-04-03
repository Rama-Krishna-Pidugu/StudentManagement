function showMessage(selector, message, type) {
    const box = $(selector);
    box.removeClass("success error").addClass(type).text(message).show();
}

function clearMessage(selector) {
    $(selector).removeClass("success error").hide().text("");
}

function handleAjaxError(xhr, selector) {
    const fallback = "Something went wrong. Please try again.";
    const response = xhr.responseJSON;
    showMessage(selector, response && response.message ? response.message : fallback, "error");
}

function logout() {
    $.ajax({
        url: "/api/auth/logout",
        method: "POST"
    }).always(function () {
        window.location.href = "/login.html";
    });
}

function fetchCurrentUser(onSuccess) {
    $.ajax({
        url: "/api/auth/me",
        method: "GET",
        success: function (response) {
            onSuccess(response.data);
        },
        error: function () {
            window.location.href = "/login.html";
        }
    });
}
