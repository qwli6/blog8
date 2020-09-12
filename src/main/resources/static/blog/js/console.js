
document.getElementById('btn-logout').addEventListener('click', function(){
    fetch("/logout", {
        method: 'POST',
        headers: new Headers({
            'Content-Type':'application/json;charset=UTF-8'
        })
    }).then(res => res.json())
        .catch(error => console.error(error))
        .then(response => {
            console.log(JSON.stringify(response));
            window.location.href = rootPath;
        });
})