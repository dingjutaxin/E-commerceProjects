package com.itmayiedu.controller;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class OrderController {
	private static final String ORDER = "order";
	
	@RequestMapping("/order")
	public String order() {
		return ORDER;
	}

}
