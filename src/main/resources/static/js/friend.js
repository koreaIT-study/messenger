
function getFriendList() {
	// 전체 친구 목록 가져오기
	// getFriends
	const userId = $('#myId').val();
	jsParamAjaxCall('GET', '/getFriends', { userId: userId }, function(response) {
		const errno = response.errno;
		if (errno === 0) {
			const friendList = response.data;
			let friendListHtml = "";

			for (let i = 0; i < friendList.length; i++) {
				friendListHtml += "<li id='" + friendList[i].friendId + "' data-rid='" + (friendList[i].roomId ?? '') + "' data-uid='" + friendList[i].friendId + "' data-group='N' ondblclick='connect(this);'>"
				friendListHtml += "<div class='friend-box'>"
				friendListHtml += "<div class='friend-profil'></div>"
				friendListHtml += "<div class='friend-title'>" + friendList[i].name + "</div>"
				friendListHtml += "<div class='friend-msg'>상메상메상메</div>"
				friendListHtml += "</div>"
				friendListHtml += "</li>"

			}
			$('#friend-list-box').html(friendListHtml);
		} else {
			_cmmnAlert.getFailed();
		}
	});
}

function getRoomList() {
	// 전체 채팅방 목록 가져오기
	jsParamAjaxCall('GET', '/chat/getRoomList', {}, function(response) {
		console.log(response)
		let roomListHtml = "";

		for (let i = 0; i < response.length; i++) {
			roomListHtml += `<li data-rId=${response[i].roomId} ondblclick="connect(this)">`;
			roomListHtml += '<div class="friend-box">';
			roomListHtml += '<div class="friend-profil"></div>';
			roomListHtml += '<div class="friend-title">';
			roomListHtml += response[i].roomName + '<span class="chatRoomLi">' + response[i].cnt + '</span>';
			roomListHtml += '<span class="chatRoomLi right time">' + response[i].time + '</span></div>';
			roomListHtml += '<div class="friend-msg">' + response[i].message + '</div></div></li>';
		}

		$('#room-list-box').html(roomListHtml);
	});
}

function getFriendListMenu() {

	$('#friend-list-box').show();
	$('#room-list-box').hide();
}
function getChatRoomListMenu() {
	$('#friend-list-box').hide();
	$('#room-list-box').show();
}
function chatRoomHide() {
	$('#chat').hide();
}

window.onload = function() {
	document.getElementById('friendListBtn').click();
	getFriendList();
	getRoomList();
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
	let roomId = $(el).data('rid');
	let userId = $(el).data('uid');
	if (!roomId) { // 친구목록에서 들어오는 경우
		// 친구의 id(userId)로 roomId(1:1 톡방)을 찾아야한다.
		// roomId = 1; // example
		// server 쪽에서 roomId를 못찾으면 roomId만들고 roomId return
		let param = {
			roomName: $(el).data('group'),
			isGroup: $(el).data('group'),
			userId: [userId, $('#myId').val()],
		};
		jsAjaxPostJsonCall('/chat/room', param, (response) => {
			$(el).data('rid', response.roomId);
			roomId = response.roomId;
		})
	}

	return roomId;
}

function getMessages(roomId) {
	// roomId가 있으면 messages전부 가져와서 view에 뿌려주는 logic 필요
	jsParamAjaxCall('GET', '/get-chat-message/' + roomId, {}, function(response) {
		let messages = response;
		console.log(messages);


		let messageHtml = "";
		let html = "";

		test = messages;
		for (let i = 0; i < messages.length; i++) {
			if (i != 0 && messages[i - 1].writer != messages[i].writer) {
				if (isMyMessage(messages[i - 1])) {
					messageHtml += myMessageTemplate(html, messages[i - 1]);
				} else {
					messageHtml += otherMessageTemplate(html, messages[i - 1]);
				}
				html = "";
			}

			if (isMyMessage(messages[i])) {
				// 내가 쓴 message
				html += myMessage(messages[i]);
			} else {
				html += otherMessage(messages[i]);
			}

		}

		// message가 남아있으면
		if (html != "") {
			if (isMyMessage(messages[messages.length - 1])) {
				messageHtml += myMessageTemplate(html, messages[messages.length - 1]);
			} else {
				messageHtml += otherMessageTemplate(html, messages[messages.length - 1]);
			}
		}

		$('#chat_msg_template').html(messageHtml);

	});
}

// message를 subscribe해서 view에 뿌려주는 method
function subMessage(message) {
	let wrap = document.getElementById('chat_msg_template');
	let lastChildDiv = wrap.lastChild;
	let lastWriter = "";
	if (lastChildDiv != null) {
		lastWriter = wrap.lastChild.dataset.uid;
		//lastWriter = lastChildDiv.querySelector('.chat_person_name').textContent;
	}

	if (lastWriter == message.writer) {
		if (isMyMessage(message)) {
			lastChildDiv.innerHTML += myMessage(message).trim();
		}
		else {
			let chatPerson = lastChildDiv.querySelector('.chat_person');
			chatPerson.innerHTML += otherMessage(message).trim();
		}
	} else {
		if (isMyMessage(message))
			wrap.innerHTML += myMessageTemplate(myMessage(message).trim(), message);
		else
			wrap.innerHTML += otherMessageTemplate(otherMessage(message).trim(), message);
	}
}

function isMyMessage(message) {
	const userId = $('#myId').val();
	if (userId == message.writer) return true;

	return false;
}


function myMessage(message) {
	let myMessage = "<div class='my_chat_flexable'>";
	myMessage += "<span class='no_read_cnt'>2</span><span class='write_date'>";
	myMessage += message.timestamp + "</span>";
	myMessage += "<div class='my_chat'>" + message.message + "</div></div>";

	return myMessage;
}

function myMessageTemplate(messages, message) {
	let myTemplate = "<div class='my_chat_wrapper' data-uid='" + message.writer + "'>";
	myTemplate += messages;
	myTemplate += "</div>";

	return myTemplate;
}

function otherMessage(message) {
	let otherMessage = "<div class='chat_msg_flexable'>";
	otherMessage += "<div class='chat_msg'>" + message.message + "</div>";
	otherMessage += "<span class='no_read_cnt'>1</span><span class='write_date'>";
	otherMessage += message.timestamp + "</span></div>";

	return otherMessage;
}

function otherMessageTemplate(messages, message) {
	let otherTemplate = "<div class='chat_person_wrap' data-uid='" + message.writer + "'>";
	otherTemplate += "<div class='chat_person_profile'>";
	otherTemplate += "<img src='img/anonymous_profile.png'></div>";
	otherTemplate += "<div class='chat_person'><div class='chat_person_name'>";
	otherTemplate += message.writerName + "</div>";
	otherTemplate += messages;
	otherTemplate += "</div></div>";

	return otherTemplate;
}

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

	var userId = $('#myId').val();
	var userName = $('#myName').val();

	var sockJs = new SockJS("/stomp/chat");
	var stomp = Stomp.over(sockJs);

	stomp.connect({}, function() {
		console.log("STOMP Connection");

		// 메세지를 받을 때
		// subscribe(path, callback)
		stomp.subscribe("/sub/chat/room/" + roomId, function(chat) {
			let obj = JSON.parse(chat.body);
			subMessage(obj);
			console.log("dddd::" + chat.body)
			//$("#chat_msg_template").append(obj.message);
		})

		// 채팅방 목록 관리
		stomp.subscribe("/sub/chat/roomList/" + roomId, function(chat) {
			let obj = JSON.parse(chat.body);
			console.log("objLLL" + chat.body)
			/*$("#chat_msg_wrap").append(obj.message);*/
		})


		// 메세지 보낼때
		// send(path, header, message)
		//	let param = JSON.stringify({ roomId: roomId, writer: userName });
		//	stomp.send("/pub/chat/enter", {}, param);
	})

	sessionContainer.push(roomId);

	$("#chat_writer").on("keyup", (e) => {
		e.preventDefault();
		if (e.keyCode != 13) return;
		if (e.shiftKey) return;

		let $textArea = $("#chat_writer");
		let param = {
			roomId: roomId,
			writer: userId,
			message: $textArea.val(),
			writerName: userName
		};
		$textArea.val('');
		stomp.send("/pub/chat/message", {}, JSON.stringify(param));

	})

}




