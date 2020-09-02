const Toast = Swal.mixin({
    toast: true,
    position: 'top-end',
    showConfirmButton: false,
    timer: 800,
    timerProgressBar: true,
    onOpen: (toast) => {
        toast.addEventListener('mouseenter', Swal.stopTimer)
        toast.addEventListener('mouseleave', Swal.resumeTimer)
    }
})

function toast(info) {
    Toast.fire({
        icon: 'success',
        title: info === null || info === '' ? '操作成功' : info
    });
}

function alertError(errors){
    Swal.fire({
        title: errors.errors.msg,
        text: errors.errors.code,
        icon: 'error',
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: '确定',
        showCancelButton: true,
        cancelButtonText: '取消'
    });
}