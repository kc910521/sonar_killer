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
import java.util.concurrent.atomic.AtomicInteger;
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
	//com.kayak.pay.utils
	private static final String BASE_UT_PACK = "com.kayak.frame.util";
	private static final String CONSTS_PACK_NUM = "ConstMagicNum";
	private static final String CONSTS_PACK_STR = "ConstFix";
	private static final File PROP_NUMBER = new File(
			"C:\\Users\\freek\\workspace\\sonarkiller\\src\\main\\java\\ind\\sonarkiller\\json\\tjson_num");
	//  "F:\\cashier-manage-api\\src\\com\\kayak\\pay\\utils\\ConstMagicNum.java"
	//  "F:\\cashier-manage-api\\src\\com\\kayak\\pay\\utils\\ConstFix.java"
	//  
	//  
	private static final File CLASS_NUMBER = new File(
			"F:\\cashier-baixin\\src\\com\\kayak\\frame\\util\\ConstMagicNum.java");
	private static final File PROP_STR = new File(
			"C:\\Users\\freek\\workspace\\sonarkiller\\src\\main\\java\\ind\\sonarkiller\\json\\tjson");
	private static final File CLASS_STR = new File(
			"F:\\cashier-baixin\\src\\com\\kayak\\frame\\util\\ConstFix.java");

	// run here
	public static void main(String[] args) throws FileNotFoundException {
		// "F:\\cashier-baixin\\src\\com\\kayak"
		fd("F:\\cashier-baixin\\src\\com");//F:\\cashier-manage-api\\src\\com//F:\\cashier-baixin\\src\\com
		// F:\cashier-baixin\src\com\kayak\consumer\service\HostConsumer.java
		// rmTypeSpecDmOper(new
		// File("F:\\cashier-baixin\\src\\com\\kayak\\consumer\\service\\PaycoreConsumer.java"));
		// #替换常量 ##########################

//		Map<String, Object> numberDict = addNumberToContent();
//		Map<String, Object> strDict = addTxtToContent();
//		Map<String, Object> numberDictX = findDictByCode(CLASS_NUMBER);
/*		FILE_LIST.forEach(f -> {
			String content = Tools.readTxtFile(f);
			String res1 = removeNumberByDict(content, numberDict);
			res1 = removeStrByDict(res1, strDict);
			if (!content.equals(res1)) {
				Tools.writeToFile(f, res1);
			}
		});*/
		AtomicInteger ai = new AtomicInteger(0);
		// #消除：注释代码，空方法，类型指定 #######
		FILE_LIST.forEach(f -> {
			Chunker chunker = new Chunker();
			chunker.setFile(f);
			AbsDecorder ad = rmCodeBlkComment(chunker);
			String finalCode = ad.doWork(null);
			if (finalCode != null && !chunker.getFileContent().equals(finalCode)) {
				Tools.writeToFile(f, finalCode);
				ai.addAndGet(1);
			}
//			String content = Tools.readTxtFile(f);
//			rmAA(content, f);
		});
		System.out.println(ai.addAndGet(0));
//		FILE_LIST.forEach(f -> {
//			Chunker chunker = new Chunker();
//			chunker.setFile(f);
//			AbsDecorder ad = rmTypeSpecDmOper(rmCodeBlkComment(rmTypeSpecDmOper(chunker)));
//			String finalCode = ad.doWork(null);
//			if (finalCode != null) {
//				Tools.writeToFile(f, finalCode);
//			}
//			String content = Tools.readTxtFile(f);
//			rmAA(content, f);
//		});
//		File ff = new File("F:\\cashier-baixin\\src\\com\\kayak\\frame\\action\\ActionService.java");
//		String content = Tools.readTxtFile(ff);
//		rmAA(content,ff);

		// ################this is training grounds

		// Map<String,Object> amap = new HashMap<>(); amap.put("XX2", 2);
		// amap.put("XX6", 6); amap.put("XX4", 4); amap.put("XX9", 9);
		// amap.put("XX0", 0);
		// File f = new File(
		// "F:\\cashier-manage-api\\src\\com\\kayak\\frame\\util\\ValidateUtil.java"
		// );
		// String content = Tools.readTxtFile(f); String res =
		// removeNumberByDict(content, amap); System.out.println(res);

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

	private static AbsDecorder rmCodeBlkComment(Chunk cker) {
		return new ReplacerDec(cker, "//(.*[\\.|=|if|\\{|\\}|\\(|\\)|;|TODO].*)\\n", "/** we were code:$1 **/\n");
	}
	
	private static void rmAA(String jcontent,File f){
		//com.kayak.frame.util.
		String reg = "(((?!import).)*)com\\.kayak\\.frame\\.util\\.([ConstFix|ConstMagicNum].*)";
		String reg2 = "import\\s+com\\.kayak\\.frame\\.util\\.(((?!options).)*);";//import com.kayak.frame.util.
		String reg3 = "import\\scom\\.kayak\\.frame\\.util\\.options\\.Options";
		String CST_OP = "HEHE_WCNM_OK";
		if (Tools.strHas(jcontent, reg)){
			boolean repOpt = false;
			if (Tools.strHas(jcontent, reg3)){
				repOpt = true;
				jcontent = Tools.strReplace(jcontent, reg3, CST_OP);
			}
			jcontent = Tools.strReplace(jcontent, reg2, "");
			jcontent = Tools.strReplace(jcontent, reg, "$1$3");
			String reg4import = "(^package.*;)(.*)";
			jcontent = Tools.strReplace(jcontent, reg4import, "$1\nimport com.kayak.frame.util.*;\n$2");
			if (repOpt){
				jcontent = Tools.strReplace(jcontent, CST_OP, "import com.kayak.frame.util.options.Options");
			}
			Tools.writeToFile(f, jcontent);
		}
		
	}

	// 递归会浪费太多空间
	private static String removeStrByDict(String fcontent, Map<String, Object> amap) {
		boolean isChanged = false;
		for (String key : amap.keySet()) {
			String theVal = amap.get(key).toString();
			if (Tools.strHas(fcontent, "\"" + theVal + "\"")) {
				fcontent = Tools.strReplace(fcontent, "\"" + theVal + "\"", App.CONSTS_PACK_STR + "." + key);
				isChanged = true;
			} else {
			}
		}
		if (isChanged){
			String reg4import = "(^package.*;)(.*)";
			fcontent = Tools.strReplace(fcontent, reg4import, "$1\nimport "+ BASE_UT_PACK +".*;\n$2");
		}
		return fcontent;
	}

	// ([\\s|=|>|<|\\(|,|\\+|-|\\*|;|\\)|{|}|\\[|\\]|:])
	private static String removeNumberByDict(String fcontent, Map<String, Object> amap) {
		String regPt1 = "([\\s|=|>|<|\\(|,|\\+|-|\\*|;|\\)|:])";

		int[] a = new int[] { 1, 2 };
		System.out.println(a[0]);
		boolean isChanged = false;
		for (String key : amap.keySet()) {

			String theVal = amap.get(key).toString();
			String reg2 = "(.*\\[)" + theVal + "(\\].*)";
			String reg3 = "(.*\\{)" + theVal + "(\\}.*\\s;)";
			
			String reg = regPt1 + theVal + regPt1;
			if (Tools.strHas(fcontent, reg)) {
				fcontent = Tools.strReplace(fcontent, reg, "$1" + App.CONSTS_PACK_NUM + "." + key + "$2");
				isChanged = true;
			}
			if (Tools.strHas(fcontent, reg2)) {
				fcontent = Tools.strReplace(fcontent, reg2, "$1" + App.CONSTS_PACK_NUM + "." + key + "$2");
				isChanged = true;
			}
			if (Tools.strHas(fcontent, reg3)) {
				fcontent = Tools.strReplace(fcontent, reg3, "$1" + App.CONSTS_PACK_NUM + "." + key + "$2");
				isChanged = true;
			}
		}
		if (isChanged){
			//(public.*class.+\\s+.*\\{.*)
			String reg4import = "(^package.*;)(.*)";
			fcontent = Tools.strReplace(fcontent, reg4import, "$1\nimport "+ BASE_UT_PACK +".*;\n$2");
		}
		return fcontent;
	}

	static int i = 0;

	/**
	 * 得到类中的变量组map
	 * 
	 * @param constClazzFile
	 * @return
	 */
	private static Map<String, Object> findDictByCode(File constClazzFile) {
		Map<String, Object> amap = new HashMap<>();
		String code = Tools.readTxtFile(constClazzFile);
		String[] arr1s = code.split("public static final [a-zA-Z\\d_]+\\s+");
		for (String a1 : arr1s) {
			if (a1.contains("=")) {
				String[] inTmps = a1.split("=");
				amap.put(inTmps[0].trim(), inTmps[1].trim().replace(";", ""));
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
			String key = "N_" + pm + "_" + Tools.randomStr(6);
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
