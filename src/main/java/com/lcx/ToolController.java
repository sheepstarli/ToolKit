package com.lcx;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class ToolController {

	@RequestMapping("/tool/t1")
	public void t1(HttpServletRequest request, HttpServletResponse response) throws IOException, InterruptedException {
		log.info("/tool/t1");
		ServletOutputStream outputStream = response.getOutputStream();
		outputStream.write("hahaha".getBytes());
		outputStream.close();
		log.info("/too/t1 out put stream closed");
		log.info("/too/t1 going to get input stream");
		ServletInputStream inputStream = request.getInputStream();
		log.info("/too/t1 get input stream and going to sleep");
		log.info("/too/t1 sleep end");
		byte[] byteAyy = new byte[1000];
		int size = inputStream.read(byteAyy);
		log.info("/too/t1 size:{}", size);
		String str = new String(byteAyy);
		log.info("/tool/t1 getStr:{}", str);
		Thread.sleep(10000L);
	}
	
	@Data
	public static class ResponseData {
		public static final String SUCCESS = "success";
		public static final String ERROR = "error";
		
		private String result = SUCCESS;
	}
	
	@RequestMapping(value = "/events/{eventType}", method = RequestMethod.POST)
	public ResponseEntity<ResponseData> events(@PathVariable("eventType") String eventType,
			@RequestBody String body) {
		log.info("receive {}: {}", eventType, body);
		return new ResponseEntity<ToolController.ResponseData>(new ResponseData(), HttpStatus.OK);
	}
}
