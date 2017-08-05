package ind.sonarkiller.dec;

import ind.sonarkiller.utils.Tools;

public class ReplacerDec extends AbsDecorder {
	
	private String regExp;
	
	// 被替换为
	private String repStr;

	public ReplacerDec(Chunk ck, String regExp,String repStr) {
		super(ck, regExp);
		this.regExp = regExp;
		this.repStr = repStr;
		// TODO Auto-generated constructor stub
	}

	@Override
	public String doWork(String code) {
		System.out.println("ReplacerDec:" + regExp);
		// TODO Auto-generated method stub
		String tmpCode = super.doWork(code);
		if (Tools.strHas(tmpCode, this.regExp)) {
			return Tools.strReplace(tmpCode, this.regExp, this.repStr);
			//add more
		}
		return tmpCode;
	}

	
}
