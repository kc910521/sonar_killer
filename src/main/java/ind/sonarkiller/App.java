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
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * Hello world!
 *
 */
public class App 
{

	private static List<File> FILE_LIST = null;

	//com/kayak/frame/afs
	public static void main(String[] args) {
		FILE_LIST = fd("F:\\cashier-baixin\\src\\com\\kayak\\frame\\afs");// src/com/kayak/pay/action
		FILE_LIST.stream().forEach(f -> {
			try {
				Map<String, Object> constsMap = addTxtToContent();
				removeStrBy(f, constsMap);
				fillBlankFunc(f);
				rmCodeBlkComment(f);
				//
				Map<String,Long> constsNbMap = addNumberToContent();
				removeNumberBy(f, constsNbMap);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		System.out.println("total I/O:" + TOTAL_CG);
		//test
//		rmCodeBlkComment(new File("F:\\cashier-baixin\\src\\com\\kayak\\frame\\action\\ActionService.java"));
		
//		Map<String,Object> hmap = new HashMap<>();
//		hmap.put("xxx1", "10");
//		hmap.put("xxx2", "60");
//		hmap.put("xxx3", "9");
//		removeNumberBy(new File("F:\\cashier-baixin\\src\\com\\kayak\\frame\\dao\\DaoService.java"), hmap);
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
				System.out.println("f");
			}
		}
		writeToFile(file, fcontent);
	}
	//[\\s|=|>|<|\\(|,|\\+|-|\\*]+
	private static void removeNumberBy(File file , Map<String, Long> amap) {
		String fcontent = App.readTxtFile(file);
		boolean hasChange = false;
		
		for (String key : amap.keySet()){
			String theVal = amap.get(key).toString();
			String reg = "[\\s|=|>|<|\\(|,|\\+|-|\\*]+" + theVal + "";
			if (strHas(fcontent, reg)) {
				String rf = strFind(fcontent, reg).replaceAll(theVal, "");
				System.out.println("true");
				hasChange = true;
				fcontent = strReplace(fcontent, reg, rf + "com.kayak.frame.util.ConstMagicNum." + key);
			}else {
				System.out.println("f");
			}
		}
		writeToFile(file, fcontent);
	}
	
	//处理重复魔法数静态资源和映射
	private static Map<String, Long> addNumberToContent() throws FileNotFoundException {
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
		Map<String,Long> resMap = new HashMap<>();
		
		allParms.forEach(pm -> {
			String key = randomStr(10) + "_" + (i++);
			resMap.put(key, pm);
			allSent.append("public final static int " + key + " = " + pm + ";\n");
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
			allSent.append("public final static String " + key + " = \"" + pm + "\";\n");
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
		// 去掉空方法，覆盖为注释
		if (strHas(content, "\\{\\t*\\}")){
			System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx" + f.getName());
			String newJaCode = strReplace(content, "\\{\\t*\\}", "{/** i am blank **/}");
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

	private static List<File> fd(String fileDir) {//  MTest.fileList
		List<File> fileList = new ArrayList<File>();
		File file = new File(fileDir);
		File[] files = file.listFiles();// 获取目录下的所有文件或文件夹
		if (files == null) {// 如果目录为空，直接退出
			return null;
		}
		// 遍历，目录下的所有文件
		for (File f : files) {
			if (f.isFile() 
					&& f.getName().endsWith(".java") 
					&& !f.getName().startsWith("ConstFix") 
					&& !f.getName().startsWith("MTest")
					&& !f.getName().startsWith("ConstMagicNum")
					) {
				fileList.add(f);
			} else if (f.isDirectory()) {
				System.out.println(f.getAbsolutePath());
				fd(f.getAbsolutePath());
			}
		}
		for (File f1 : fileList) {
			System.out.println(f1.getName());
		}
		return fileList;
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
