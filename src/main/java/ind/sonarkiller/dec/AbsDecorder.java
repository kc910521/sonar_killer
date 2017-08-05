package ind.sonarkiller.dec;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.Gson;

public abstract class AbsDecorder implements Chunk {

	private String regExp;
	
	private Chunk ck;

	public AbsDecorder(Chunk ck, String regExp) {
		this.regExp = regExp;
		this.ck = ck;
	}

	@Override
	public String doWork(String code) {
		// TODO Auto-generated method stub
		System.out.println("home work");
		return ck.doWork(code);
	}







}
