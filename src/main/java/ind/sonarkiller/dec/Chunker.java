package ind.sonarkiller.dec;

import java.io.File;

import ind.sonarkiller.utils.Tools;

public class Chunker implements Chunk {
	
	private File file;
	
	private String fileContent = null;
	
	

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}
	
	public String getFileContent() {
		return fileContent;
	}

	@Override
	public String doWork(String code) {
		if (this.fileContent == null) {
			this.fileContent = Tools.readTxtFile(file);
		}
		return this.fileContent;
	}

}
