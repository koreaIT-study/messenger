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
	$('#chat').hide();
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

function openRoom(el) {

	let rId = $(el).data('rid');
	let isGroup = $(el).data('group');
	let roomName = $('#roomName').val();

	// rId 없으면 채팅창 만들기
	/*if(!rId){
		jsAjaxPostJsonCall('/chat/room', {roomName : roomName,isGroup : isGroup}, function (response) {
			console.log(response)
			$(this).data('rId',response.roomId);
			rId = response.roomId;
		})
	}*/

	console.log(rId + ":" + isGroup + ":" + roomName);

	// 채팅창 열기
	let param = JSON.stringify({ roomId: rId, writer: $('#userName').val() });
	stomp.send("/pub/chat/enter", {}, param);
}

var sessionContainer = [];

function connect(el) {
	// roomId 찾는 logic 필요
	var roomId = searchRoomId(el);
	$('#chat').show();
	if (sessionContainer.includes(roomId)) {
		// 이미 session 연결 됬으므로 view만 보여준다.
	} else {
		enterRoom(el, roomId);
	}
	
	getMessages(roomId);
}


function searchRoomId(el) {
	// 기존 채팅방이 있으면 db에서 채팅방을 만들때 id로 만들거니깐 roomId가 있을거고
	// 없으면 uuid로 생성
	// 친구목록에서 채팅방 만들 땐 db조회 logic 필요
	//var roomId = $("#roomId").val() ?? 'tester';

	let roomId = $(el).data('rid');
	let uid
	if (!roomId) { // 친구목록에서 들어오는 경우
		// 친구의 id(userId)로 roomId(1:1 톡방)을 찾아야한다.
		// server 쪽에서 roomId를 못찾으면 roomId만들고 roomId return
		let param = {
			roomName : $(el).data('group'),
			isGroup : $('#roomName').val(),
			userId : [],
		};
		jsAjaxPostJsonCall('/chat/room', param, (response) =>{
			$(el).data('rId',response.roomId);
			roomId = response.roomId;
		})
	}
	
	return roomId;
}


function getMessages(roomId){
	// roomId가 있으면 messages전부 가져와서 view에 뿌려주는 logic 필요
}

//$(function() {
function enterRoom(el, roomId) {
	/*	// 기존 채팅방이 있으면 db에서 채팅방을 만들때 id로 만들거니깐 roomId가 있을거고
		// 없으면 uuid로 생성
		// 친구목록에서 채팅방 만들 땐 db조회 logic 필요
		//var roomId = $("#roomId").val() ?? 'tester';
	
		var roomId = $(el).data('rid');
		var userId = $(el).data('uid') ?? '1';
		var roomName = $("#roomName").val() ?? 'tester_name';
		var userName = $('#userName').val() ?? 'tester_userNAme';
	
		if (!roomId) { // 친구목록에서 들어오는 경우
			// 친구의 id(userId)로 roomId(1:1 톡방)을 찾아야한다.
	
			// server 쪽에서 찾으면 roomId만들고, roomId와 메시지들 db에서 전부 가져와서 뿌려주는 logic
			// server 쪽에서 roomId를 못찾으면 roomId만들고 roomId return
	
		} else {
			// 채팅방에서 들어가는 경우
			// server쪽으로 message들 전부 가져와서 view에 뿌려줘야함
		}*/

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
		
		stomp.subscribe("/sub/chat/user/" + userId, function(chat) {
			let obj = JSON.parse(chat.body);
			$("#chat_msg_wrap").append(obj.message);
		})

		// 메세지 보낼때
		// send(path, header, message)
		let param = JSON.stringify({ roomId: roomId, writer: userName });
		stomp.send("/pub/chat/enter", {}, param);
	})

	sessionContainer.push(roomId);

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

}
//})




