const sockJs = new SockJS("/stomp/chat");
const stomp = Stomp.over(sockJs);
let msgSubscription = "";

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
				friendListHtml += "<li id='" + friendList[i].friendId + "' data-rid='" + (friendList[i].roomId ?? '') + "' data-uid='" + friendList[i].friendId + "' data-group='N' >"
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
			roomListHtml += '<span class="chatRoomLi right time">' + response[i].timestamp.split('.')[0] + '</span></div>';
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


	//	let sockJs = new SockJS("/stomp/chat");
	//	let stomp = Stomp.over(sockJs);

	const userId = $('#myId').val();
	// 채팅방 목록 관리
	stomp.connect({}, function() {
		stomp.subscribe("/sub/chat/roomList/" + userId, function(chat) {
			let obj = JSON.parse(chat.body);
			updateChatRoom(obj);
		})
	})

	// friend list에 connect event
	let friendList = $('#friend_list').find('#friend-list-box').find('li');
	for (let i = 0; i < friendList.length; i++) {
		friendList[i].addEventListener('dblclick', () => connect(friendList[i]));
	}
	//	ondblclick='connect(this);'
	// message 보내기
	$("#chat_writer").on("keyup", (e) => {
		let header = document.getElementById('chat_header');

		e.preventDefault();
		if (e.keyCode != 13) return;
		if (e.shiftKey) return;
		if ($("#chat_writer").val().trim() == '' || $("#chat_writer").val() == '\n') {
			$("#chat_writer").val('');
			$("#chat_writer").focus();
			return;
		}

		let $textArea = $("#chat_writer");

		let param = {
			roomId: header.dataset.rid,
			writer: $('#myId').val(),
			message: $textArea.val(),
			writerName: $('#myName').val()
		};
		$textArea.val('');
		console.log("메시지 보냄")
		stomp.send("/pub/chat/message", {}, JSON.stringify(param));

	})

}

function makeChatRoom() {

}

var sessionContainer = [];

function connect(el) {
	let room = searchRoom(el);
	let roomId = room.roomId;
	$('#chat').show();
	if (!enterRoom(el, roomId)) return;
	searchRoomInfo(roomId);
	getMessages(roomId);
}

function searchRoom(el) {
	let roomId = $(el).data('rid');
	let userId = $(el).data('uid');
	let room = "";

	if (!roomId) { // 친구목록에서 들어오는 경우
		// 친구의 id(userId)로 roomId(1:1 톡방)을 찾아야한다.
		// roomId = 1; // example
		// server 쪽에서 roomId를 못찾으면 roomId만들고 roomId return
		let param = {
			roomName: $('#myName').val() + "," + $(`#${el.id} div div.friend-title`).text(),
			isGroup: $(el).data('group'),
			userId: [userId, $('#myId').val()],
		};
		jsAjaxPostJsonCall('/chat/room', param, (response) => {
			room = response;
		})
	} else {
		jsParamAjaxCall('GET', '/chat/room?roomId=' + roomId, {}, function(response) {
			room = response;
		})
	}

	return room;
}

function searchRoomInfo(roomId) {
	// roomId로 채팅방 정보를 찾아주는 method
	jsParamAjaxCall('GET', '/chat/room?roomId=' + roomId, {}, function(response) {
		console.log("search room info")
		console.log(response)
		let title = $('.chat_title').children();
		title[0].innerHTML = response.roomName;
		title[1].innerHTML = "멤버 " + response.cnt;
	});
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

	// 채팅 맨밑으로 내림
	let wrap = document.getElementById('chat_msg_template');
	let lastChildDiv = wrap.lastChild;
	if (lastChildDiv != null)
		lastChildDiv.lastChild.scrollIntoView();
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
	console.log("메세지 받았음!!");

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

	lastChildDiv.lastChild.scrollIntoView();

}

function isMyMessage(message) {
	const userId = $('#myId').val();
	if (userId == message.writer) return true;

	return false;
}


function myMessage(message) {
	let myMessage = "<div class='my_chat_flexable'>";
	myMessage += "<span class='no_read_cnt'>2</span><span class='write_date'>";
	myMessage += `${message.timestamp.split('.')[0]}</span>`;
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
	otherMessage += message.timestamp.split('.')[0] + "</span></div>";

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
	let header = document.getElementById('chat_header');
	let curRoomId = header.dataset.rid;
	if (curRoomId == roomId) return false;

	header.dataset.rid = roomId;


	if (msgSubscription != '') {
		msgSubscription.unsubscribe();
	}



	msgSubscription = stomp.subscribe("/sub/chat/room/" + roomId, function(chat) {
		let obj = JSON.parse(chat.body);
		subMessage(obj);
	})

	console.log("msgSubscription:::")
	console.log(msgSubscription);
	//roomIdSessionCtx.push(roomid);
	return true;
}

function updateChatRoom(message) {
	let roomList = document.getElementById('room-list-box');
	let flag = false;

	for (let room of roomList.children) {
		console.log(room.dataset.rid)
		if (message.roomId == room.dataset.rid) {
			flag = true;
			break;
		}
	}

	console.log("make room")
	console.log(message)
	// 채팅방 없으면 만들어주기
	if (!flag) {

		let roomHtml = "";
		roomHtml += `<li data-rId=${message.roomId} ondblclick="connect(this)">`;
		roomHtml += '<div class="friend-box">';
		roomHtml += '<div class="friend-profil"></div>';
		roomHtml += '<div class="friend-title">';
		roomHtml += message.roomName + '<span class="chatRoomLi">' + message.cnt + '</span>';
		roomHtml += '<span class="chatRoomLi right time">' + message.timestamp.split('.')[0] + '</span></div>';
		roomHtml += '<div class="friend-msg">' + message.message + '</div></div></li>';

		$(roomList).prepend(roomHtml);
	}

	let roomListChild = $('#room-list-box').children();

	for (let i = 0; i < roomListChild.length; i++) {
		if (roomListChild[i].dataset.rid == message.roomId) {
			$(roomListChild[i]).find('.chatRoomLi.right.time').html(message.timestamp.split('.')[0]);
			$(roomListChild[i]).find('.friend-msg').html(message.message);
			roomList.prepend($(roomListChild)[i]);
			break;
		}
	}


}


