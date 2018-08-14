import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.appleyk.Application;
import com.appleyk.entity.Tile;
import com.appleyk.entity.User;
import com.appleyk.repository.TileReponsitory;
import com.appleyk.repository.UserRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes=Application.class)// 指定spring-boot的启动类 
public class TestSql {

	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private TileReponsitory tileReponsitory;
	
	
	@Test
	public void TestSaveTile(){
		Tile tile = new Tile(1002L,220,351,12,758L,"google");
		byte[] data = getBytes("E:\\tiles\\hn\\zz\\16\\l_16_x_53289_y_25978.jpg");
		tile.setData(data);
		tileReponsitory.insert(tile);
		System.err.println("保存成功");
	}
	
	@Test
	public void TestDeleteTile(){
		tileReponsitory.delete(1001L);
		System.err.println("删除成功！");
	}
	
	 /** 
     * 获得指定文件的byte数组 
     */ 
	public  byte[] getBytes(String filePath){  
        byte[] buffer = null;  
        try {  
            File file = new File(filePath);  
            FileInputStream fis = new FileInputStream(file);  
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);  
            byte[] b = new byte[1000];  
            int n;  
            while ((n = fis.read(b)) != -1) {  
                bos.write(b, 0, n);  
            }  
            fis.close();  
            bos.close();  
            buffer = bos.toByteArray();  
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        return buffer;  
    }   
	
	/**
	 * 根据二进制 转成 文件
	 * @param data
	 * @param fileName
	 * @param saveDir
	 * @throws Exception
	 * @throws IOException
	 */
	public static void saveAsFile(byte[] data, String fileName, String saveDir) throws Exception, IOException {

		File dirFile = new File(saveDir);
		// 不存在 创建级联目录
		if (!dirFile.exists()) {
			dirFile.mkdirs();
		}

		String newFilePath = saveDir + File.separator + fileName;
		File file = new File(newFilePath);
		if (!file.exists()) {
			file.createNewFile();
		}

		FileOutputStream fos = new FileOutputStream(file);
		fos.write(data);
		if (fos != null) {
			fos.close();
		}

		System.err.println("文件："+newFilePath+" -- 保存成功！");
	}
	
	/**
	 * 插入一条记录
	 */
	@Test
	public void TestSave(){	
		userRepository.insert(new User(1001L,"appleyk",27,"男"));
		System.err.println("保存成功！");
	}
	
	/**
	 * 查询全部User实体
	 */
	@Test
	public void TestFindAll(){	
		List<User> users = userRepository.findAll();
		System.err.println("size: "+users.size()+","+users.get(0).getName());
	}
	
	
	/**
	 * 根据name查询User
	 */
	@Test
	public void TestFindByName(){	
		List<User> users = userRepository.findByName("appleyk");
		System.err.println("size: "+users.size());
	}
	
	/**
	 * 根据id删除对应User实体
	 */
	@Test
	public void TestDelete(){
		userRepository.delete(1001L);
		System.err.println("删除成功！");
	}
}
