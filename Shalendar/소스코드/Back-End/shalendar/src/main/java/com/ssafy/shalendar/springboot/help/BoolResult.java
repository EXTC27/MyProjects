package com.ssafy.shalendar.springboot.help;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoolResult{
	// 값
	private boolean result;
    // 행동 이름
	private String name;
	// 상태
	private String state;
		
}
