package com.teamride.messenger.client.repository;

import com.teamride.messenger.client.dto.ChatRoomDTO;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.*;

@Repository
public class ChatRoomRepository {

    // 나중에 DB로 바꾸면 필요 없어짐
    private Map<String, ChatRoomDTO> chatRoomDTOMap;

    // 생성자가 호출되었을 때 bean을 초기화 시켜주는 어노테이션
    @PostConstruct
    private void init(){
        chatRoomDTOMap = new LinkedHashMap<>();
    }

    public List<ChatRoomDTO> findAllRooms(){
        // 나중에 server 에 요청을 보내 DB 에서 chatRoom List 를 꺼내오면 될듯
        ArrayList<ChatRoomDTO> chatRoomDTOS = new ArrayList<>(chatRoomDTOMap.values());
        Collections.reverse(chatRoomDTOS);

        return chatRoomDTOS;
    }

    public ChatRoomDTO findRoomById(String roomId){
        // 이부분도 나중에 server 에 요청 보내서 데이터 가져오기
        return chatRoomDTOMap.get(roomId);
    }

    public ChatRoomDTO createChatRoomDTO(String roomName){
        ChatRoomDTO room = ChatRoomDTO.create(roomName);
        // server 에 요청을 보내 생성된 채팅방 정보 저장
        chatRoomDTOMap.put(room.getRoomId(), room);

        return room;
    }
}
