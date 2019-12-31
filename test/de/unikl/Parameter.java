package de.unikl;


import factory.*;

public class Parameter {



	public static boolean LOGGING = false;
	public static int TESTS = 100;
	public static int HIGH = TESTS*10;
	public static int MEDIUM = TESTS*5;
	public static int LOW = TESTS*1;
	public static int TIMEOUT = 4; //seconds

	public static boolean full = true;

	//scope of implemented highlevel strategies to test
	public static final EHighlevel[] HIGHLEVEL = new EHighlevel[]{EHighlevel.Chaff,EHighlevel.DPLLSimple};
	public static final EDecide[] DECIDE = new EDecide[]{EDecide.MiniSAT,EDecide.SLIS};
	public static final EForget[] FORGET = new EForget[]{EForget.ForgetSimple,EForget.ForgetNever,EForget.ForgetRandomLarge, EForget.ForgetRandomShort};
	public static final ELearn[] LEARN = new ELearn[]{ELearn.LearnSimple,ELearn.LearnNever};
	public static final ESetOfClauses[] FORMULA = new ESetOfClauses[]{ESetOfClauses.DataMixed};
	public static final ETrail[] TRAIL = ETrail.values();

//	public static final EHighlevel[] HIGHLEVEL = new EHighlevel[]{EHighlevel.Chaff};
//	public static final EDecide[] DECIDE = new EDecide[]{EDecide.MiniSAT};
//	public static final EForget[] FORGET = new EForget[]{EForget.ForgetSimple};
//	public static final ELearn[] LEARN = new ELearn[]{ELearn.LearnSimple};
//	public static final ESetOfClauses[] FORMULA = new ESetOfClauses[]{ESetOfClauses.Data2WL};
//	public static final ETrail[] TRAIL = ETrail.values();
}
