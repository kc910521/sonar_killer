package ind.sonarkiller.dec;

import java.util.Map;

import ind.sonarkiller.utils.Tools;

public class ReplacerByDictDec extends AbsDecorder {
	
	private String regExp;
	
	// 被替换为
	private String repStr;
	
	private Map<String, Object> dict = null;

	public ReplacerByDictDec(Chunk ck, String regExp,String repStr) {
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
		if (dict == null || dict.isEmpty()){
			System.err.println("dict is null?");
			return tmpCode;
		}
		for (String key : dict.keySet()){
			String theVal = dict.get(key).toString();
			if (Tools.strHas(tmpCode, regExp)) {
				tmpCode = Tools.strReplace(tmpCode, regExp, "com.kayak.frame.util.ConstFix." + key);
			}else {
			}
		}
		return tmpCode;
		
	}

	
}
