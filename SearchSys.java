package sample04;

public abstract class SearchSys {
	//split配列順（0:全国地方公共団体コード,1:旧郵便番号（5桁）,2:郵便番号（7桁）,3:カナ住所1,4:カナ住所2,5:カナ住所3,6:住所1,7:住所2,8:住所3）
	private final int POSTCODE = 2;	//Data型のcode
	private final int KANA1 = 3;	//Data型のkana
	private final int KANA2 = 4;	//Data型のkana
	private final int KANA3 = 5;	//Data型のkana
	private final int ADDRESS1 = 6;	//Data型のaddress1
	private final int ADDRESS2 = 7;	//Data型のaddress2
	private final int ADDRESS3 = 8;	//Data型のaddress2

	// 抽象メソッド
	protected abstract void init();
	protected abstract void search();
	protected abstract void sort();
	
	// Data型の追加データを返す・・・郵便番号(7桁）,カナ住所,住所1（都道府県）,住所2（市区町村）,住所3（市区町村以下）
	protected Data setData(String[] items) {
		return new Data(items[POSTCODE].trim(),
				items[KANA1].trim()+items[KANA2].trim()+items[KANA3].trim(),
				items[ADDRESS1].trim(),
				items[ADDRESS2].trim(),
				items[ADDRESS3].trim());
	}
}
