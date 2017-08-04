package ind.sonarkiller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sql.rowset.spi.SyncResolver;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * Hello world!
 * 4 jdk1.8+
 *
 */
public class App 
{

	private static List<File> FILE_LIST = new LinkedList<>();

	//com/kayak/frame/afs
	public static void main(String[] args) throws FileNotFoundException {
		//"F:\\cashier-baixin\\src\\com\\kayak"
		 fd("F:\\cashier-baixin\\src\\com\\kayak");// src/com/kayak/pay/action
//F:\cashier-baixin\src\com\kayak\consumer\service\HostConsumer.java
//		rmTypeSpecDmOper(new File("F:\\cashier-baixin\\src\\com\\kayak\\consumer\\service\\PaycoreConsumer.java"));
		
		 
		 FILE_LIST.forEach(f -> {
//			 rmTypeSpecDmOper(f);
//			 fillBlankFunc(f);
		 });
		 
		 
		//
		//ypa();
		System.out.println("total I/O:" + TOTAL_CG);
	}
	
	
	private static void rmTypeSpecDmOper(File f) {
		String content = App.readTxtFile(f);
		String reg = "(=\\s*new\\s.*[List|Map]<).+(>\\(\\))";
		if (strHas(content, reg)) {
			String[] contents = content.split("\\n");
			String newJaCode = strReplace(content, reg, "$1$2");
			writeToFile(f, newJaCode);
		}
	}
	
	//executer
	private static void ypa() throws FileNotFoundException{
		
		Map<String, Object> constsMap = addTxtToContent();
		Map<String,Object> constsNbMap = addNumberToContent();
		FILE_LIST.stream().forEach(f -> {
			try {
				removeStrBy(f, constsMap);
				fillBlankFunc(f);
				rmCodeBlkComment(f);
				//
				removeNumberBy(f, constsNbMap);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		
		//test
//		rmCodeBlkComment(new File("F:\\cashier-baixin\\src\\com\\kayak\\frame\\action\\ActionService.java"));
		
//		Map<String,Object> hmap = new HashMap<>();
//		hmap.put("xxx1", 10);
//		hmap.put("xxx3", 10000);
//		hmap.put("xxx2", 60);
//		removeNumberBy(new File("F:\\cashier-baixin\\src\\com\\kayak\\frame\\redis\\RedisService.java"), hmap);
	}

	private static void fd(String fileDir) {//  MTest.fileList
		File file = new File(fileDir);
		File[] files = file.listFiles();// 获取目录下的所有文件或文件夹
		if (files == null) {// 如果目录为空，直接退出
			return;
		}
		// 遍历，目录下的所有文件
		for (File f : files) {
			if (f.isFile() 
					&& f.getName().endsWith(".java") 
					&& !f.getName().startsWith("ConstFix") 
					&& !f.getName().startsWith("MTest")
					&& !f.getName().startsWith("ConstMagicNum")
					&& !f.getName().startsWith("Viracct")
					) {
				FILE_LIST.add(f);
			} else if (f.isDirectory()) {
				System.out.println(f.getAbsolutePath());
				fd(f.getAbsolutePath());
			}
		}
		for (File f1 : FILE_LIST) {
			System.out.println(f1.getName());
		}
	}
	
	static int i = 0;
	
	private static void rmCodeBlkComment(File f) {
		String content = App.readTxtFile(f);
		String reg = "//.*([\\.|=|if|\\{|\\}|\\(|\\)|;|TODO]).*\\n";
		if (strHas(content, reg)) {
			System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx" + f.getName());
			String newJaCode = strReplace(content, reg, "/** we were code **/\n");
			//add more
			writeToFile(f, newJaCode);
		}
	}
	
	private static void removeStrBy(File file , Map<String, Object> amap) {
		String fcontent = App.readTxtFile(file);
		boolean hasChange = false;
		for (String key : amap.keySet()){
			String theVal = amap.get(key).toString();
			if (strHas(fcontent,"\"" + theVal + "\"")) {
				System.out.println("true");
				hasChange = true;
				fcontent = strReplace(fcontent,"\"" + theVal + "\"", "com.kayak.frame.util.ConstFix." + key);
			}else {
			}
		}
		writeToFile(file, fcontent);
	}
	//[\\s|=|>|<|\\(|,|\\+|-|\\*]+
	private static void removeNumberBy(File file , Map<String, Object> amap) {
		String fcontent = App.readTxtFile(file);
		boolean hasChange = false;
		
		for (String key : amap.keySet()){
			System.out.println(key);
			String theVal = amap.get(key).toString();
			// @FIXME
			String reg = "[\\s|=|>|<|\\(|,|\\+|-|\\*|;|\\)]" + theVal + "[\\s|=|>|<|\\(|,|\\+|-|\\*|;|\\)]";
			if (strHas(fcontent, reg)) {
				String[] rfs = strFind(fcontent, reg).split(theVal);
				String sfx = null;
				if (rfs.length == 2){
					sfx = rfs[1];
				}else if (rfs.length == 1){
					sfx = "";
				}else{
					sfx = "";
					System.err.println("rfs " + rfs.length);
				}
				System.out.println("true");
				hasChange = true;
				fcontent = strReplace(fcontent, reg, rfs[0] + "com.kayak.frame.util.ConstMagicNum." + key + sfx);
			}else {
			}
		}
		writeToFile(file, fcontent);
	}
	
	//处理重复魔法数静态资源和映射
	private static Map<String, Object> addNumberToContent() throws FileNotFoundException {
		File f = new File("F:\\cashier-baixin\\src\\com\\kayak\\frame\\util\\ConstMagicNum.java");
		File propf = new File("C:\\Users\\freek\\workspace\\sonarkiller\\src\\main\\java\\ind\\sonarkiller\\json\\tjson_num");
		if (!f.exists() || !propf.exists()) {
			throw new FileNotFoundException("file?");
		}
		String json = App.readTxtFile(propf);
		String content = App.readTxtFile(f);
		System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx" + f.getName());
		// System.out.println(content);
		@SuppressWarnings({ "unchecked", "unused" })
		JsonObject amap = fromJson(json, JsonObject.class);
		@SuppressWarnings({ "unused", "rawtypes" })
		String issues = amap.get("issues").toString();
		JsonArray jarray = fromJson(issues, JsonArray.class);
		Set<Long> allParms = new HashSet<>();
		StringBuffer allSent = new StringBuffer("");
		jarray.forEach(jobj -> {
			String tmp = jobj.getAsJsonObject().get("message").getAsString();
			String metaNum = tmp.replace("Assign this magic number ", "")
					.replace(" to a well-named constant, and use the constant instead.", "");
			try {
				Long mnum = Long.valueOf(metaNum.replace("L", ""));
				allParms.add(mnum);
			} catch(Exception e) {
				e.printStackTrace();
			}
		});
		Map<String,Object> resMap = new HashMap<>();
		
		allParms.forEach(pm -> {
			String key = randomStr(10) + "_" + (i++);
			resMap.put(key, pm);
			allSent.append("public static final int " + key + " = " + pm + ";\n");
		});
		String newJavaCode = strReplace(content, "\\{\\n*", "{" + allSent);
		System.out.println(newJavaCode);
		writeToFile(f, newJavaCode);
		// System.out.println(strHas(content,"\\{\\t*\\}"));
		System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
		return resMap;
	}

	
	//处理重复字符静态资源和映射
	private static Map<String, Object> addTxtToContent() throws FileNotFoundException {
		File f = new File("F:\\cashier-baixin\\src\\com\\kayak\\frame\\util\\ConstFix.java");
		File propf = new File("C:\\Users\\freek\\workspace\\sonarkiller\\src\\main\\java\\ind\\sonarkiller\\json\\tjson");
		if (!f.exists() || !propf.exists()) {
			throw new FileNotFoundException("file?");
		}
		String json = App.readTxtFile(propf);
		String content = App.readTxtFile(f);
		System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx" + f.getName());
		// System.out.println(content);
		@SuppressWarnings({ "unchecked", "unused" })
		JsonObject amap = fromJson(json, JsonObject.class);
		@SuppressWarnings({ "unused", "rawtypes" })
		String issues = amap.get("issues").toString();
		JsonArray jarray = fromJson(issues, JsonArray.class);
		Set<String> allParms = new HashSet<>();
		StringBuffer allSent = new StringBuffer("");
		jarray.forEach(jobj -> {
			String tmp = jobj.getAsJsonObject().get("message").getAsString();
			allParms.add(tmp.replace("Define a constant instead of duplicating this literal \"", "")
					.replaceAll("\" \\d+ times\\.", ""));
		});
		Map<String,Object> resMap = new HashMap<String,Object>();
		
		allParms.forEach(pm -> {
			String key = randomStr(10) + "_" + (i++);
			resMap.put(key, pm);
			allSent.append("public static final String " + key + " = \"" + pm + "\";\n");
		});
		String newJavaCode = strReplace(content, "\\{\\n*", "{" + allSent);
		System.out.println(newJavaCode);
		writeToFile(f, newJavaCode);
		// System.out.println(strHas(content,"\\{\\t*\\}"));
		System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
		return resMap;
	}



	private static void fillBlankFunc(File f) {
		String content = App.readTxtFile(f);
		// System.out.println(content);
		String reg = "\\{[\\t|\\n]*\\}";
		// 去掉空方法，覆盖为注释
		if (strHas(content, reg)){
			System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx" + f.getName());
			String newJaCode = strReplace(content, reg, "{/** i am blank **/}");
			writeToFile(f, newJaCode);
		}
	}

	// ############### tools
	
	private static String randomStr(int length) {
		String base = "ABCDEFGHIJKLMNO_QRSTUVWXYZ";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}
	public static <T> T fromJson(String str, Class<T> type) {
		Gson gson = new Gson();
		return gson.fromJson(str, type);
	}


	
	private static long TOTAL_CG = 0;

	private static void writeToFile(File file, String content) {
		BufferedWriter bw = null;
		try {
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			bw = new BufferedWriter(fw);
			bw.write(content);
			System.out.println("Done");
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			TOTAL_CG ++;
			if (bw != null){
				try {
					bw.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	private static String readTxtFile(File file) {
		StringBuffer sb = null;
		BufferedReader bre = null;
		try {
			String encoding = "utf-8";
			if (file.isFile() && file.exists()) { // 判断文件是否存在
//				read = new InputStreamReader(new FileInputStream(file), encoding);// 考虑到编码格式
//				BufferedReader bufferedReader = new BufferedReader(read);
				
				bre = new BufferedReader(new FileReader(file));
				String lineTxt = null;
				sb = new StringBuffer();
				while ((lineTxt = bre.readLine()) != null) {
					sb.append(lineTxt + "\n");
				}
				return sb.toString();
			} else {
				System.out.println("找不到指定的文件");
			}
		} catch (Exception e) {
			System.out.println("读取文件内容出错");
			e.printStackTrace();
		} finally {
			try {
				if (bre != null) {
					bre.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;

	}

	private static String strReplace(String str, String regFrom, String strTo) {
		return str.replaceAll(regFrom, strTo);
	}
	private static String findPt(String str, String regFrom, int pos) {
		Pattern p = Pattern.compile(regFrom);
		Matcher m = p.matcher(str);
		while (m.find()) {
			System.out.println(m.group(pos));
			System.out.println(m.group(2));
		}
		return "";
	}

	private static boolean strHas(String str, String regFrom) {
		Pattern pattern = Pattern.compile(regFrom);
		// 忽略大小写的写法
		// Pattern pat = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(str);
		return matcher.find();
	}
	
	private static String strFind(String str, String regFrom) {
		Pattern pattern = Pattern.compile(regFrom);
		// 忽略大小写的写法
		// Pattern pat = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(str);
		if (matcher.find()){
			return matcher.group();
		}
		return "";
	}
}
