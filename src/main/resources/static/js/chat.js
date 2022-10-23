
function sendFile(){
    var sendFileName = document.getElementById('send_file_name');
    var sendFileVal = document.getElementById('send_file');

    var files = sendFileVal.files;

    var fileName ="";

    for (var i = 0; i < files.length; i++) {
                
        fileName = fileName+files[i].name + "\n";
    }
    console.log(fileName)
    sendFileName.innerHTML = fileName;
}

