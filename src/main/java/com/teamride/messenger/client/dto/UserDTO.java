package com.teamride.messenger.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
	private int id;
	private String email;
	private String name;
	private String pwd;
	private String profileImg;
}
