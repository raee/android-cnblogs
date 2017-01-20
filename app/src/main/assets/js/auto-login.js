function autoLogin(){

    var api = loginBridge;

    $("#tip_btn").bind("DOMSubtreeModified",function(){
        var msg = $("#tip_btn").text();
        if(!msg || msg.length<=0 || msg.indexOf("登录成功")!=-1){
            return;
        }
        api.onLoginError(msg);
    });
    $("#tip_captcha_code_input").bind("DOMSubtreeModified",function(){
        var msg = $("#tip_captcha_code_input").text();
        api.onLoginCodeError(msg);
    });

    var userName = api.getUserName();
    var password = api.getPassword();
    var code=api.getCode();
    $("#input1").val(userName);
    $("#input2").val(password);
    $("#captcha_code_input").val(code);
    $("#signin").click();


}

autoLogin();