const rootPath = '/';

window.onload = function(){
    document.getElementById('inputUsername').focus();
}

let url = new URL(window.location.href);
let redirectUrl = url.searchParams.get("redirectUrl");


document.getElementById('btn-login').addEventListener('click', function(){
    submitLogin();
});

document.onkeydown = function(e){
    let theEvent = window.event || e;
    let keyCode = theEvent.keyCode || theEvent.which || theEvent.charCode;
    if (keyCode === 13) {
        submitLogin();
    }
}

function submitLogin(){
    const username = document.getElementById('inputUsername').value;

    if(username === null || username === '' || username.trim().length === 0){
        document.getElementById('errorMsg').style.display = 'block';
        document.getElementById('errorMsg').innerText = "用户名不能为空";
        setTimeout(function(){
            document.getElementById('errorMsg').style.display = 'none';
        }, 2000);

        return;
    }

    const password = document.getElementById('inputPassword').value;

    if(password === null || password === '' || password.trim().length === 0){
        document.getElementById('errorMsg').style.display = 'block';
        document.getElementById('errorMsg').innerText = "密码不能为空";
        setTimeout(function(){
            document.getElementById('errorMsg').style.display = 'none';
        }, 2000);
        return;
    }

    const data = {
        'username': username,
        'password': password,
        'rememberMe': false
    }

    document.getElementById('btn-login').disabled = true;

    fetch(rootPath + 'api/token', {
        method: 'POST',
        body: JSON.stringify(data),
        headers: new Headers({
            'Content-Type':'application/json;charset=UTF-8'
        })
    }).then(res => {
        return res.json();
    }).catch(error => {
        console.error("登录异常:" + JSON.stringify(error));
        document.getElementById('btn-login').disabled = false;
        document.getElementById('errorMsg').style.display = 'block';
        document.getElementById('errorMsg').innerText = "登录异常";
        setTimeout(function(){
            document.getElementById('errorMsg').style.display = 'none';
        }, 2000);
    }).then(response => {
        if(response.code === 200){
            document.getElementById('errorMsg').style.display = 'none';

            //用户名密码不可编辑
            document.getElementById('inputUsername').disabled = true;
            document.getElementById('inputPassword').disabled = true;

            //登录按钮不显示
            document.getElementById('btn-login').style.display = 'none';

            //二次验证码允许输入
            document.getElementById('inputTopCode').disabled = false;

            //二次验证显示
            document.getElementById('inputTopCode').style.display = 'block';
            document.getElementById('btn-top-auth').style.display = 'block';
            document.getElementById('inputTopCode').focus();

        } else {
            document.getElementById('errorMsg').style.display = 'block';
            let innerText = '认证失败';
            if(response.errors !== null && response.errors !== undefined){
                innerText = response.errors.msg;
            }
            document.getElementById('errorMsg').innerText = innerText;

            setTimeout(function(){
                document.getElementById('errorMsg').style.display = 'none';
            }, 2000);
        }
        document.getElementById('btn-login').disabled = false;
    });
}

document.getElementById('btn-top-auth').addEventListener('click', function(){
    let topCode = document.getElementById('inputTopCode').value;
    if(topCode === null || topCode === '' || topCode.trim().length === 0){
        document.getElementById('errorMsg').style.display = 'block';
        document.getElementById('errorMsg').innerText = "请输入二次认证码";
        setTimeout(function(){
            document.getElementById('errorMsg').style.display = 'none';
        }, 2000);
        return;
    }

    document.getElementById('btn-top-auth').disabled = true;

    fetch(rootPath + "session", {
        method: 'POST',
        body: JSON.stringify({"topCode": topCode}),
        headers: new Headers({
            'Content-Type':'application/json;charset=UTF-8'
        })
    }).then(response => {
        //用户名密码不可编辑
        document.getElementById('inputUsername').disabled = false;
        document.getElementById('inputPassword').disabled = false;

        //登录按钮不显示
        document.getElementById('btn-login').style.display = 'block';

        //二次验证码允许输入
        document.getElementById('inputTopCode').disabled = true;

        //二次验证显示
        document.getElementById('inputTopCode').style.display = 'none';
        document.getElementById('btn-top-auth').style.display = 'none';

        return response.json();

    }).catch(error => {
        console.log(error);
        //清空全部
        document.getElementById("inputUsername").value = '';
        document.getElementById('inputUsername').style.display = 'block';
        document.getElementById("inputPassword").value = '';
        document.getElementById('inputPassword').style.display = 'block';
        document.getElementById('btn-login').style.display = 'block';

        //二次认证相关
        document.getElementById('inputTopCode').value = '';
        document.getElementById('inputTopCode').style.display = 'none';
        document.getElementById('btn-top-auth').style.display = 'none';
        document.getElementById('btn-top-auth').disabled = false;
    }).then(res => {
        if(res.success) {
            if (redirectUrl != null) {
                window.location.href = redirectUrl;
            } else {
                window.location.href = rootPath;
            }
        } else {
            document.getElementById('errorMsg').style.display = 'block';
            let innerText = '认证失败';
            if(res.errors !== null && res.errors !== undefined){
                innerText = res.errors.msg;
            }
            document.getElementById('errorMsg').innerText = innerText;

            //认证失败, 重新填写用户名密码
            document.getElementById("inputUsername").value = '';
            document.getElementById('inputUsername').style.display = 'block';
            document.getElementById("inputPassword").value = '';
            document.getElementById('inputPassword').style.display = 'block';
            document.getElementById('btn-login').style.display = 'block';

            //二次认证相关
            document.getElementById('inputTopCode').value = '';
            document.getElementById('inputTopCode').style.display = 'none';
            document.getElementById('btn-top-auth').style.display = 'none';
            document.getElementById('btn-top-auth').disabled = false;

            setTimeout(function(){
                document.getElementById('errorMsg').style.display = 'none';
            }, 2000);
        }
    });
});
