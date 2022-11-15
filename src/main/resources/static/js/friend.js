// 나중에 getFriendList 랑 getChatRoomList 합치면 될듯
/*function getFriendList(){
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
}*/

function getFriendList() {

	$('#friend-list-box').show();
	$('#room-list-box').hide();
}
function getChatRoomList() {
	$('#friend-list-box').hide();
	$('#room-list-box').show();
}

window.onload = function() {
	document.getElementById('friendListBtn').click();
}

function popOpen() {
	var modalPop = $('.modal-wrap');
	var modalBg = $('.modal-bg');

	$(modalPop[0]).show();
	$(modalBg[0]).show();
}

function popClose() {
	var modalPop = $('.modal-wrap');
	var modalBg = $('.modal-bg');

	$(modalPop[0]).hide();
	$(modalBg[0]).hide();

}



$(function() {
	var roomId = $("#roomId").val() ?? 'tester';
	var roomName = $("#roomName").val() ?? 'tester_name';
	var userName = $('#userName').val() ?? 'tester_userNAme';

	var sockJs = new SockJS("/stomp/chat");
	var stomp = Stomp.over(sockJs);

	stomp.connect({}, function() {
		console.log("STOMP Connection");

		// 메세지를 받을 때
		// subscribe(path, callback)
		stomp.subscribe("/sub/chat/room/" + roomId, function(chat) {
			let obj = JSON.parse(chat.body);
			$("#chat_msg_wrap").append(obj.message);
		})

		// 메세지 보낼때
		// send(path, header, message)
		let param = JSON.stringify({ roomId: roomId, writer: userName });
		stomp.send("/pub/chat/enter", {}, param);
	})

	$("#chat_writer").on("keyup", (e) => {
		e.preventDefault();
		if (e.keyCode != 13) return;
		if (e.shiftKey) return;

		let $textArea = $("#chat_writer");
		let param = {
			roomId: roomId,
			writer: userName,
			message: $textArea.val()
		};
		$textArea.val('');
		stomp.send("/pub/chat/message", {}, JSON.stringify(param));

	})


})




