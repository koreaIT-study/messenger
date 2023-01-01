
window.load(() => {
    let fileInput = document.getElementById(send_file);
    fileInput.addEventListener('onChange',sendFile);
})

function sendFile(){
    let form = new FormData();
    let fileInput = document.getElementById('send_file');

    for(let file of fileInput.files){
        form.append('files', file);
    }

    $.ajax({
        url : "/file",
        type : `POST`,
        processData : false,
        // contentType : 'multipart/form-data',
        contentType : false,
        data : form,
        success : (response) => {
            console.log(`파일잘보냄`);
        },
        error : (err) => {
            console.log(err.responseText);
        }
    })
    fileInput.value = '';
    // fileInput.files = new FileList();
}

$(function(){
    $('#chat_writer').val('');
});
