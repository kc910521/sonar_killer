package ind.sonarkiller.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.Gson;

public final class Tools {
	
	private Tools(){}
	
	// ############### tools
	public static String readTxtFile(File file) {
		StringBuffer sb = null;
		BufferedReader bre = null;
		try {
			String encoding = "utf-8";
			if (file != null && file.isFile() && file.exists()) { // 判断文件是否存在
				// read = new InputStreamReader(new FileInputStream(file),
				// encoding);// 考虑到编码格式
				// BufferedReader bufferedReader = new BufferedReader(read);

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
	
	public static String strFind(String str, String regFrom) {
		Pattern pattern = Pattern.compile(regFrom);
		// 忽略大小写的写法
		// Pattern pat = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(str);
		if (matcher.find()) {
			return matcher.group();
		}
		return "";
	}
	
	public static boolean strHas(String str, String regFrom) {
		Pattern pattern = Pattern.compile(regFrom);
		// 忽略大小写的写法
		// Pattern pat = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(str);
		return matcher.find();
	}
	
	public static String randomStr(int length) {
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

	public static void writeToFile(File file, String content) {
		BufferedWriter bw = null;
		try {
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			bw = new BufferedWriter(fw);
			bw.write(content);
			System.out.println("Done");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			TOTAL_CG++;
			if (bw != null) {
				try {
					bw.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}



	public static String strReplace(String str, String regFrom, String strTo) {
		return str.replaceAll(regFrom, strTo);
	}

	public static String findPt(String str, String regFrom, int pos) {
		Pattern p = Pattern.compile(regFrom);
		Matcher m = p.matcher(str);
		while (m.find()) {
			System.out.println(m.group(pos));
			System.out.println(m.group(2));
		}
		return "";
	}

}
