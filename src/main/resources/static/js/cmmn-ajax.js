function handleErrorStatus(jqXHR) {
    //if (jqXHR.responseText.indexOf("_LoginPage") != -1) {
    if (jqXHR.responseText == undefined) {
        alert("시스템에서 자동 로그아웃 처리되었습니다.");
        top.location.href = "/";
    } else if (jqXHR.responseText.indexOf("메인화면") != -1) {
        alert("시스템에서 자동 로그아웃 처리되었습니다.");
        top.location.href = "/";
    } else if (jqXHR.responseText == "") {
        //실시간 데이터가 없을시
    }else {
        alert("죄송합니다. 시스템 오류가 발생했습니다.\n잠시 후 다시 시도하거나 관리자에게 문의하십시오.");
    }
}

function jsParamAjaxCall(ajaxType, url, params, callbackFunc, callbackFailFunc) {

    $.ajax({
        type: ajaxType,
        async: false,
        url: url,
        data: params,
        dataType: "json",
        cache: false,
        beforeSend: function (xhr, setting) {
            if (setting && setting.async == true) {}
        },
        success: function (data, textStatus, jqXHR) {
            $('.overlay').attr('style', 'display:none');
            if (callbackFunc) {
                eval(data);
                callbackFunc(data, jqXHR);
            } else {
                // do nothing
            }
        },
        error: function (jqXHR, textStatus, errorThrown) {
            $('.overlay').attr('style', 'display:none');
            if (callbackFailFunc) {
                eval(callbackFailFunc + "(jqXHR);");
            } else {
                handleErrorStatus(jqXHR);
            }
        }
    });
}

function jsAjaxPostJsonCall(url, params, callbackFunc, callbackFailFunc) {

    var ajax_data = (typeof params === 'object') ? JSON.stringify(params) : params;

    $.ajax({
        type: 'POST',
        async: false,
        url: url,
        data: ajax_data,
        dataType: "json",
        contentType: "application/json; charset=utf-8",
        cache: false,
        beforeSend: function (xhr, setting) {
            if (setting && setting.async == true) {}
        },
        success: function (data, textStatus, jqXHR) {
            $('.overlay').attr('style', 'display:none');
            if (callbackFunc) {
                eval(data);
                callbackFunc(data, jqXHR);
            } else {
                // do nothing
            }
        },
        error: function (jqXHR, textStatus, errorThrown) {
            $('.overlay').attr('style', 'display:none');
            if (callbackFailFunc) {
                eval(callbackFailFunc + "(jqXHR);");
            } else {
                handleErrorStatus(jqXHR);
            }
        }
    });
}



//ajax file upload
function jsAjaxForm(ajaxType, url, formId, callbackFunc) {
    var iframeName = 'f' + Math.floor(Math.random() * 99999);
    var $iFrame = $('<iframe id="' + iframeName + '" name="' + iframeName + '" style="display:none" src="about:blank"></iframe>');
    $iFrame.appendTo('body');

    var form = $('#' + formId);
    form.attr('method', ajaxType);
    form.attr('target', iframeName);
    form.attr('action', url);
    form.attr("encoding", "multipart/form-data");
    form.attr("enctype", "multipart/form-data");

    form.on('submit', function () {});

    $("#" + iframeName).load(function () {
        var i = document.getElementById(iframeName);
        var d = null;
        if (i.contentDocument) {
            d = i.contentDocument;
        } else if (i.contentWindow) {
            d = i.contentWindow.document;
        } else {
            d = window.frames[id].document;
        }

        if (d.location.href == "about:blank") {
            return;
        }

        if (typeof (callbackFunc) == 'function') {
            try {
                var response = eval("(" + d.body.innerHTML + ")");
                callbackFunc(response);
            } catch (e) {
                handleErrorStatus(d.body.innerHTML);
            }
        }
    });
}