// 나중에 getFriendList 랑 getChatRoomList 합치면 될듯
function getFriendList(){
    // 요청을 보내 list를 가져옴
    let list;
    let html = '';
    for(let i = 0; i < 10; i++){
        html += '<li>'
        html    += '<div class="friend-box">'
        html        += '<div class="friend-profil"></div>'
        html        += '<div class="friend-title">' + '친구친구'+ '</div>'
        html        += '<div class="friend-msg">' + '상메상메상메' + '</div>'
        html     += '</div>'
        html += '</li>'
    }

    let listBox = document.getElementById('list-box');
    let friendListBox = document.getElementById('friend-list-box');
    if(friendListBox) friendListBox.remove();

    let ul = document.createElement('ul');
    ul.setAttribute('id', 'friend-list-box');
    ul.innerHTML = html;
    listBox.appendChild(ul);
}

function getChatRoomList(){
    // 요청을 보내 list를 가져옴
    let list;
    let html = '';
    for(let i = 0; i < 10; i++){
        html += '<li>'
        html    += '<div class="friend-box">'
        html        += '<div class="friend-profil"></div>'
        html        += '<div class="friend-title">' + '채팅방'+ '<span class="chatRoomLi">'+3+'</span><span class="chatRoomLi right time">'+'오후 7:30'+'</span></div>'
        html        += '<div class="friend-msg">' + '마지막 내용 가 나 다 라 마 바 사 아 자 차 카 타 파 하' + '</div>'
        html     += '</div>'
        html += '</li>'
    }

    let listBox = document.getElementById('list-box');
    let friendListBox = document.getElementById('friend-list-box');
    if(friendListBox) friendListBox.remove();
    
    let ul = document.createElement('ul');
    ul.setAttribute('id', 'friend-list-box');
    ul.innerHTML = html;
    listBox.appendChild(ul);
}

window.onload = function(){
    document.getElementById('friendListBtn').click();
}

function popOpen() {
    var modalPop = document.getElementsByClassName('.modal-wrap');
    var modalBg = document.getElementsByClassName('modal-bg'); 

    modalPop[0].setAttribute('display','block');
    modalBg[0].setAttribute('display','block');
}

 function popClose() {
    var modalPop = document.getElementsByClassName('.modal-wrap');
    var modalBg = document.getElementsByClassName('modal-bg'); 

    modalPop[0].setAttribute('display','none');
    modalBg[0].setAttribute('display','none');

}