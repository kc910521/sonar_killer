package ind.sonarkiller.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.Gson;

public final class Tools {
	
	private Tools(){}
	
	// ############### tools
	public static String readTxtFile(File file) {
		StringBuffer sb = new StringBuffer();
		char[] charCache = null;
		//字符编码
		Charset charset = Charset.forName("utf-8");
		CharBuffer cbuf = CharBuffer.allocate(1024 * 5);
		ByteBuffer bbuf = ByteBuffer.allocate(1024 * 5);
		CharsetDecoder charDecoder = charset.newDecoder();
		try (FileInputStream raf = new FileInputStream(file)){
			FileChannel fcn = raf.getChannel();
			try {
				while (fcn.read(bbuf) != -1) {
					bbuf.flip();
					charDecoder.decode(bbuf, cbuf, true);
					cbuf.flip();
					charCache = new char[cbuf.length()];
					while (cbuf.hasRemaining()){
						cbuf.get(charCache);
						String str = new String(charCache);
						System.out.println(":"+str);
						sb.append(str);
						bbuf = ByteBuffer.wrap(str.getBytes());
					}
//				bbuf.compact();
					cbuf.clear();
					bbuf.clear();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return sb.toString();
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
		
		try (FileOutputStream raf = new FileOutputStream(file);) {
			//获取输入通道
			FileChannel fc_in = raf.getChannel();
			//读取数据到缓冲区
            ByteBuffer buf = ByteBuffer.wrap(content.getBytes("utf-8"));
            buf.put(content.getBytes("utf-8"));
            buf.flip();
            fc_in.write(buf);
//            raf.write(buf.array());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
/*		BufferedWriter bw = null;
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
		}*/
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

	public static long getTOTAL_CG() {
		return TOTAL_CG;
	}
	
	

}
