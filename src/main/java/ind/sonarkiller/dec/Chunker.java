package ind.sonarkiller.dec;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import ind.sonarkiller.utils.Tools;

public class Chunker implements Chunk {
	
	private File file;
	
	

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	@Override
	public String doWork(String code) {
		// TODO Auto-generated method stub
		return Tools.readTxtFile(file);
	}

}
