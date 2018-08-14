package com.appleyk.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.appleyk.entity.Tile;
import com.appleyk.result.ResponseMessage;
import com.appleyk.result.ResponseResult;
import com.appleyk.service.TileService;

@RestController
@RequestMapping("/rest/v1.0.1/appleyk/tile")
public class TileController {

	
	@Autowired
	private TileService tileService;
	
	@GetMapping("/query")
	public ResponseResult Query(Tile tile,HttpServletResponse response){
		
		List<Tile> tiles = tileService.findData(tile);
	    byte[] data = tiles.get(0).getData();
	    response.setContentType("image/jpg"); //设置返回的文件类型   
        OutputStream os;
		try { 
			os = response.getOutputStream();
			os.write(data);  
	        os.flush();  
	        os.close(); 
		} catch (IOException e) {
			e.printStackTrace();
		}           
		return new ResponseResult(ResponseMessage.OK);
	}
}
