package sample04;

import java.util.ArrayList;

public class Data {
	protected final String code;
	protected final String kana;
	protected final String address1;
	protected final String address2;
	protected final String address3;
	
	public Data(String code,String kana,String address1,String address2,String address3) {
		this.code = code;			//郵便番号（7桁）
		this.kana = kana;			//カナ住所1+カナ住所2+カナ住所3
		this.address1 = address1;	//住所1
		this.address2 = address2;	//住所2
		this.address3 = address3;	//住所3
	}
	
	//郵便番号検索
	public boolean HitCode(String searchnum) {
		return this.code.matches(searchnum+".*");
	}
	//漢字検索
	public boolean HitHan2(String searchaddress1,String searchaddress2) {
		return this.address1.matches(searchaddress1+".*") && (this.address2+this.address3).matches(searchaddress2+".*");
	}
	//地名部分検索
	public boolean HitHan1(String searchaddress) {
		return (this.address1+this.address2+this.address3).matches(".*"+searchaddress+".*");
	}
	
	//カナでソートしてから郵便番号でソート（郵便番号でソートしてあるが、重複した場合はカナ順に並んでいる状態）
	public static void CodeSort(ArrayList<Data> list) {
		list.sort((x , y) -> x.kana.compareTo(y.kana) );
		list.sort((x , y) -> x.code.compareTo(y.code) );
	}
	//カナでソート
	public static void KanaSort(ArrayList<Data> list) {
		list.sort((x , y) -> x.kana.compareTo(y.kana) );
	}
	
	//表示形成
	public String toString() {
		return this.code+" ： "+this.address1+this.address2+this.address3;
	}
}
