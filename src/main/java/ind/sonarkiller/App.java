package ind.sonarkiller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import ind.sonarkiller.dec.AbsDecorder;
import ind.sonarkiller.dec.Chunk;
import ind.sonarkiller.dec.Chunker;
import ind.sonarkiller.dec.ReplacerDec;
import ind.sonarkiller.utils.Tools;

/**
 * Hello world! 4 jdk1.8+
 *
 */
public class App {

	private static final List<File> FILE_LIST = new LinkedList<>();

	// 解析特定的json文件（PROP_?），得到变量，然后放入类(CLASS_?)存储
	private static final String CONSTS_PACK_NUM = "com.kayak.pay.utils.ConstMagicNum.";
	private static final String CONSTS_PACK_STR = "com.kayak.pay.utils.ConstFix.";
	private static final File PROP_NUMBER = new File(
			"C:\\Users\\freek\\workspace\\sonarkiller\\src\\main\\java\\ind\\sonarkiller\\json\\tjson_num");
	private static final File CLASS_NUMBER = new File(
			"F:\\cashier-manage-api\\src\\com\\kayak\\pay\\utils\\ConstMagicNum.java");
	private static final File PROP_STR = new File(
			"C:\\Users\\freek\\workspace\\sonarkiller\\src\\main\\java\\ind\\sonarkiller\\json\\tjson");
	private static final File CLASS_STR = new File(
			"F:\\cashier-manage-api\\src\\com\\kayak\\pay\\utils\\ConstFix.java");

	// run here
	public static void main(String[] args) throws FileNotFoundException {
		// "F:\\cashier-baixin\\src\\com\\kayak"
		fd("F:\\cashier-manage-api\\src\\com");// src/com/kayak/pay/action
		// F:\cashier-baixin\src\com\kayak\consumer\service\HostConsumer.java
		// rmTypeSpecDmOper(new
		// File("F:\\cashier-baixin\\src\\com\\kayak\\consumer\\service\\PaycoreConsumer.java"));
		// #替换常量
		Map<String, Object> numberDict = addNumberToContent();
		Map<String, Object> strDict = addTxtToContent();
//		Map<String, Object> numberDictX = findDictByCode();
		FILE_LIST.forEach(f -> {
			String content = Tools.readTxtFile(f);
			String res1 = removeNumberByDict(content, numberDict);
			res1 = removeStrByDict(res1,strDict);
			if (!content.equals(res1)){
				Tools.writeToFile(f, res1);
			}
		});
		
/*		Map<String,Object> amap = new HashMap<>();
		amap.put("XX", 0);
		amap.put("XX4", 4);
		amap.put("XX5", 5);
		amap.put("XX10", 10);
		File f = new File("F:\\cashier-manage-api\\src\\com\\kayak\\pay\\utils\\NumCheckUtil.java");
		
		String content = Tools.readTxtFile(f);
		String res = removeNumberByDict(content, amap);
		System.out.println(res);*/
		// ypa();

		// #######################neo-type invoke

		/*
		 * Chunker cker = new Chunker(); cker.setFile(new
		 * File("C:\\Users\\freek\\Desktop\\111.txt")); AbsDecorder ck = new
		 * ReplacerDec( rmTypeSpecDmOper(cker), "List", "911");
		 * System.out.println(ck.doWork(""));
		 */

		/*
		 * File f = new File("C:\\Users\\freek\\Desktop\\111.txt");
		 * Map<String,Object> amap = new HashMap<>(); amap.put("XXP", 10);
		 * amap.put("PXX", 3); amap.put("PXX", 55); String content =
		 * Tools.readTxtFile(f); String aa = removeNumberByDict(content, amap);
		 * System.out.println(aa);
		 */
		// Tools.writeToFile(f, newJaCode);

		System.out.println(Tools.getTOTAL_CG() + " files update !");
	}

	// remove new ?<?,?>()
	private static AbsDecorder rmTypeSpecDmOper(Chunk cker) {
		return new ReplacerDec(cker, "(=\\s*new\\s.*[List|Map]<).+(>\\(\\))", "$1$2");
	}

	private static AbsDecorder fillBlankFunc(Chunk cker) {
		return new ReplacerDec(cker, "\\{[\\t|\\n]*\\}", "/** i am blank **/");
	}

	private static AbsDecorder removeStrBy(Chunk cker) {
		return new ReplacerDec(cker, "\\{[\\t|\\n]*\\}", "/** i am blank **/");
	}

	private static AbsDecorder rmCodeBlkComment(Chunk cker) {
		return new ReplacerDec(cker, "//.*([\\.|=|if|\\{|\\}|\\(|\\)|;|TODO]).*\\n", "/** we were code **/\n");
	}

	// 递归会浪费太多空间
	private static String removeStrByDict(String fcontent, Map<String, Object> amap) {
		for (String key : amap.keySet()) {
			String theVal = amap.get(key).toString();
			if (Tools.strHas(fcontent, "\"" + theVal + "\"")) {
				fcontent = Tools.strReplace(fcontent, "\"" + theVal + "\"", App.CONSTS_PACK_STR + key);
			} else {
			}
		}
		return fcontent;
	}

	// [\\s|=|>|<|\\(|,|\\+|-|\\*]+
	private static String removeNumberByDict(String fcontent, Map<String, Object> amap) {
		for (String key : amap.keySet()) {
			String theVal = amap.get(key).toString();
			String reg = "([\\s|=|>|<|\\(|,|\\+|-|\\*|;|\\)|{|}|\\[|\\]|:])" + theVal + "([\\s|=|>|<|\\(|,|\\+|-|\\*|;|\\)|{|}|\\[|\\]|:])";
			if (Tools.strHas(fcontent, reg)) {
				fcontent = Tools.strReplace(fcontent, reg, "$1" + App.CONSTS_PACK_NUM + key + "$2");
			} else {
			}
		}
		return fcontent;
	}

	static int i = 0;

	private static Map<String, Object> findDictByCode(){
		Map<String,Object> amap = new HashMap<>();
		String code = Tools.readTxtFile(CLASS_NUMBER);
		String[] arr1s = code.split("public static final .+\\s+");
		for (String a1 : arr1s){
			if (a1.contains("=")){
				String[] inTmps = a1.split("=");
				amap.put(inTmps[0].trim(), inTmps[1].trim());
			}
		}
		return amap;
	}
	
	// 处理重复魔法数静态资源和映射
	private static Map<String, Object> addNumberToContent() throws FileNotFoundException {
		if (!CLASS_NUMBER.exists() || !PROP_NUMBER.exists()) {
			throw new FileNotFoundException("file?");
		}
		String json = Tools.readTxtFile(PROP_NUMBER);
		String content = Tools.readTxtFile(CLASS_NUMBER);
		System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx" + CLASS_NUMBER.getName());
		// System.out.println(content);
		@SuppressWarnings({ "unchecked", "unused" })
		JsonObject amap = Tools.fromJson(json, JsonObject.class);
		@SuppressWarnings({ "unused", "rawtypes" })
		String issues = amap.get("issues").toString();
		JsonArray jarray = Tools.fromJson(issues, JsonArray.class);
		Set<Long> allParms = new HashSet<>();
		StringBuffer allSent = new StringBuffer("");
		jarray.forEach(jobj -> {
			String tmp = jobj.getAsJsonObject().get("message").getAsString();
			String metaNum = tmp.replace("Assign this magic number ", "")
					.replace(" to a well-named constant, and use the constant instead.", "");
			try {
				Long mnum = Long.valueOf(metaNum.replace("L", ""));
				allParms.add(mnum);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		Map<String, Object> resMap = new HashMap<>();

		allParms.forEach(pm -> {
			String key = Tools.randomStr(10) + "_" + (i++);
			resMap.put(key, pm);
			allSent.append("public static final int " + key + " = " + pm + ";\n");
		});
		String newJavaCode = Tools.strReplace(content, "\\{\\n*", "{" + allSent);
		System.out.println(newJavaCode);
		Tools.writeToFile(CLASS_NUMBER, newJavaCode);
		// System.out.println(strHas(content,"\\{\\t*\\}"));
		System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
		return resMap;
	}

	// 处理重复字符静态资源和映射
	private static Map<String, Object> addTxtToContent() throws FileNotFoundException {
		if (!CLASS_STR.exists() || !PROP_STR.exists()) {
			throw new FileNotFoundException("file?");
		}
		String json = Tools.readTxtFile(PROP_STR);
		String content = Tools.readTxtFile(CLASS_STR);
		System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx" + CLASS_STR.getName());
		// System.out.println(content);
		@SuppressWarnings({ "unchecked", "unused" })
		JsonObject amap = Tools.fromJson(json, JsonObject.class);
		@SuppressWarnings({ "unused", "rawtypes" })
		String issues = amap.get("issues").toString();
		JsonArray jarray = Tools.fromJson(issues, JsonArray.class);
		Set<String> allParms = new HashSet<>();
		StringBuffer allSent = new StringBuffer("");
		jarray.forEach(jobj -> {
			String tmp = jobj.getAsJsonObject().get("message").getAsString();
			allParms.add(tmp.replace("Define a constant instead of duplicating this literal \"", "")
					.replaceAll("\" \\d+ times\\.", ""));
		});
		Map<String, Object> resMap = new HashMap<String, Object>();

		allParms.forEach(pm -> {
			String key = Tools.randomStr(10) + "_" + (i++);
			resMap.put(key, pm);
			allSent.append("public static final String " + key + " = \"" + pm + "\";\n");
		});
		String newJavaCode = Tools.strReplace(content, "\\{\\n*", "{" + allSent);
		System.out.println(newJavaCode);
		Tools.writeToFile(CLASS_STR, newJavaCode);
		// System.out.println(strHas(content,"\\{\\t*\\}"));
		System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
		return resMap;
	}

	// ############### tools
	private static void fd(String fileDir) {// MTest.fileList
		File file = new File(fileDir);
		File[] files = file.listFiles();// 获取目录下的所有文件或文件夹
		if (files == null) {// 如果目录为空，直接退出
			return;
		}
		// 遍历，目录下的所有文件
		for (File f : files) {
			if (f.isFile() && f.getName().endsWith(".java") && !f.getName().startsWith("ConstFix")
					&& !f.getName().startsWith("MTest") && !f.getName().startsWith("ConstMagicNum")
					&& !f.getName().startsWith("Viracct")) {
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

}
